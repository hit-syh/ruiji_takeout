package syh.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import syh.dto.SetMealDto;
import syh.exception.DeleteStatusException;
import syh.pojo.Result;
import syh.pojo.Setmeal;
import syh.pojo.SetmealDish;
import syh.service.SetmealDishService;
import syh.service.SetmealService;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetMealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @GetMapping("/page")
    public Result pageDish(Long page, Long pageSize, String name)
    {

        return setmealService.listDish(page,pageSize,name);
    }
//    @PostMapping
@RequestMapping(method = {RequestMethod.POST,RequestMethod.PUT})
    @Transactional
    public Result addSetMeal(@RequestBody SetMealDto setMealDto)
    {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setMealDto,setmeal);
        setmealService.saveOrUpdate(setmeal);
        Long setmealId = setmeal.getId();


        setmealDishService.lambdaUpdate().eq(SetmealDish::getSetmealId,setmealId).remove();


        setMealDto.getSetmealDishes().stream().forEach(setmealDish->{
            setmealDish.setSetmealId(setmealId);
            setmealDishService.save(setmealDish);
        });
        return Result.success("添加成功");
    }
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id)
    {
        Setmeal setmeal = setmealService.getById(id);

        SetMealDto setMealDto = new SetMealDto();
        BeanUtils.copyProperties(setmeal,setMealDto);

        List<SetmealDish> setmealDishes = setmealDishService.lambdaQuery().eq(SetmealDish::getSetmealId, setmeal.getId()).list();
        setMealDto.setSetmealDishes(setmealDishes);
        return Result.success(setMealDto);
    }
    @PostMapping("/status/{status}")
    public Result startStopStatus(@PathVariable Integer status,@RequestParam List<Long> ids)
    {
        setmealService.lambdaUpdate().in(Setmeal::getId,ids).set(Setmeal ::getStatus,status).update();
       return Result.success("修改成功");
    }
    @DeleteMapping
    @Transactional
    public Result removeByIds(@RequestParam List<Long> ids) throws DeleteStatusException {

        //状态判断
        if(setmealService.lambdaQuery().in(Setmeal::getId,ids).eq(Setmeal::getStatus,1).count()>0)
            throw new DeleteStatusException();
        //先移除setmeal_dish中
        setmealDishService.lambdaUpdate().in(SetmealDish::getSetmealId,ids).remove();

        //
        setmealService.lambdaUpdate().in(Setmeal::getId,ids).remove();
        return Result.success("删除成功");
    }
    @GetMapping("/list")
    public Result listSetmealByCategoryId(Long categoryId,Integer status)
    {
        return Result.success(setmealService.lambdaQuery().eq(Setmeal::getCategoryId,categoryId).eq(status!=null,Setmeal::getStatus,status).list());

    }
}

package syh.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import syh.dto.DishCategory;
import syh.dto.DishDto;

import syh.exception.DeleteStatusException;
import syh.pojo.Dish;
import syh.pojo.DishFlavor;
import syh.pojo.Result;
import syh.service.DishFlavorService;
import syh.service.DishService;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;

    @PostMapping()
    @CacheEvict(value ={"Category_Dish","setmeal"} ,allEntries = true)
    public Result addDish(@RequestBody DishDto dishDto)
    {
        return dishService.addDish(dishDto);
    }

    @GetMapping("/page")
    public Result pageDish(Long page,Long pageSize,String name)
    {
        return dishService.listDish(page,pageSize,name);
    }
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id)
    {
        Dish dish = dishService.getById(id);
        DishCategory dishCategory = new DishCategory();
        BeanUtils.copyProperties(dish,dishCategory);

        List<DishFlavor> dishFlavors =dishFlavorService.list(new QueryWrapper<DishFlavor>().eq("dish_id",dish.getId()));
        dishCategory.setFlavors(dishFlavors);
        return Result.success(dishCategory);
    }
    @PutMapping
    @Transactional
    @CacheEvict(value ={"Category_Dish","setmeal"} ,allEntries = true)
    public Result update(@RequestBody DishDto dishDto)
    {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDto,dish);
        Long dishId=dish.getId();
        //删除之前的口味
        QueryWrapper<DishFlavor> dishFlavorQueryWrapper = new QueryWrapper<>();
        dishFlavorQueryWrapper.eq("dish_id",dishId);
        dishFlavorService.remove(dishFlavorQueryWrapper);

        //添加新的口味

        dishDto.getFlavors().stream().forEach(dishFlavor -> {
            dishFlavor.setDishId(dishId);
            dishFlavorService.save(dishFlavor);
        });

        dishService.saveOrUpdate(dish);
        return Result.success("修改成功");
    }
    @DeleteMapping
    @CacheEvict(value ={"Category_Dish","setmeal"} ,allEntries = true)
    @Transactional
    public Result deleteByIds(@RequestParam  List<Long> ids) throws DeleteStatusException {
        for (Long id :ids)
        {
            if(dishService.lambdaQuery().eq(Dish::getId,id).eq(Dish::getStatus,1).count()>0)
                throw new DeleteStatusException();
            dishFlavorService.remove(new QueryWrapper<DishFlavor>().eq("dish_id",id));
        }
        dishService.removeByIds(ids);
        return Result.success("删除成功");
    }
    @CacheEvict(value ={"Category_Dish","setmeal"} ,allEntries = true)
    @PostMapping("/status/{status}")
    public Result startOrStopByIds(@PathVariable Integer status,@RequestParam List<Long> ids)
    {
        dishService.lambdaUpdate().in(Dish::getId,ids).set(Dish::getStatus,status).update();
        return Result.success("修改成功");
    }
    @GetMapping("/list")
    @Cacheable(value = "Category_Dish",key = "#categoryId+'-'+#name+'-'+#status")
    public Result listByCategoryId(Long categoryId,String name,Integer status)
    {
        List<Dish> dishList = dishService.lambdaQuery().like(StringUtils.isNotBlank(name), Dish::getName, name).eq(categoryId != null, Dish::getCategoryId, categoryId).eq(status != null, Dish::getStatus, status).list();
        List<DishDto> dishDtoList=dishList.stream().map(dish -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish,dishDto);
            dishDto.setFlavors(dishFlavorService.lambdaQuery().eq(DishFlavor::getDishId,dish.getId()).list());
            return dishDto;
        }).collect(Collectors.toList());


        return Result.success(dishDtoList);
    }
}

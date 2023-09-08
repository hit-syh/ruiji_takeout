package syh.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import syh.exception.DeleteCategoryException;
import syh.pojo.Category;
import syh.pojo.Dish;
import syh.pojo.Result;
import syh.pojo.Setmeal;
import syh.service.CategoryService;
import syh.service.DishService;
import syh.service.SetmealService;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;
    @GetMapping("/page")
    public Result page(Long page, Long pageSize)
    {
        return categoryService.pagelist(page,pageSize);
    }
    @PostMapping("")
    public Result addCategory(@RequestBody Category category)
    {
        categoryService.save(category);
        return Result.success("添加成功");
    }
    @DeleteMapping
    public Result deleteCategory(long ids) throws DeleteCategoryException {
        QueryWrapper<Dish> dishWrapper = new QueryWrapper<>();
        dishWrapper.eq("category_id",ids);

        QueryWrapper<Setmeal> setmealWrapper = new QueryWrapper<>();
        setmealWrapper.eq("category_id",ids);

        long count=dishService.count(dishWrapper)+setmealService.count(setmealWrapper);

        if (count ==0)
        {
            categoryService.removeById(ids);
            return Result.success("删除成功");
        }
        else
        {
            throw new DeleteCategoryException();
        }
    }
    @PutMapping
    public Result updateCategory(@RequestBody Category category)
    {
        categoryService.saveOrUpdate(category);
        return Result.success("修改成功");
    }
    @GetMapping("/list")
    public  Result listCategory(Integer type)
    {

        return Result.success(categoryService.query().eq(type!=null,"type",type).list());
    }


}

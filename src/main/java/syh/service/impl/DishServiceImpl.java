package syh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import syh.dto.DishCategory;
import syh.dto.DishDto;
import syh.pojo.Dish;
import syh.pojo.DishFlavor;
import syh.pojo.Result;
import syh.service.CategoryService;
import syh.service.DishFlavorService;
import syh.service.DishService;
import syh.mapper.DishMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author shiyu
* @description 针对表【dish(菜品管理)】的数据库操作Service实现
* @createDate 2023-09-01 10:34:58
*/
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish>
    implements DishService{
    @Autowired
    DishFlavorService dishFlavorService;
    @Autowired
    CategoryService categoryService;
    @Override
    @Transactional
    public Result addDish(DishDto dishDto) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDto,dish);
        this.save(dish);
        for(DishFlavor dishFlavor : dishDto.getFlavors())
        {
            dishFlavor.setDishId(dish.getId());
            dishFlavorService.save(dishFlavor);
        }
        return Result.success("添加成功");
    }

    @Override
    public Result listDish(Long page, Long pageSize, String name) {
        QueryWrapper<Dish> dishQueryWrapper = new QueryWrapper<>();
        dishQueryWrapper.like(StringUtils.isNotBlank(name),"name",name);
        Page<Dish> dishPage = new Page<>(page, pageSize);
        this.page(dishPage,dishQueryWrapper);

        Page<DishCategory> dishCategoryPage=new Page<>(page,pageSize);
        dishCategoryPage.setTotal(dishPage.getTotal());
        dishCategoryPage.setPages(dishPage.getPages());

        List<Dish> records = dishPage.getRecords();
        List<DishCategory> dishCategoryList=records.stream().map((item)->{
            DishCategory dishCategory = new DishCategory();
            BeanUtils.copyProperties(item,dishCategory);
            Long categoryId = item.getCategoryId();
            String categoryName = categoryService.getById(categoryId).getName();
            dishCategory.setCategoryName(categoryName);
            return dishCategory;
        }).collect(Collectors.toList());

        dishCategoryPage.setRecords(dishCategoryList);
        return Result.success(dishCategoryPage);
    }
}





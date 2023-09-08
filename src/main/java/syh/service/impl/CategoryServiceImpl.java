package syh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import syh.pojo.Category;
import syh.pojo.Dish;
import syh.pojo.Result;
import syh.pojo.Setmeal;
import syh.service.CategoryService;
import syh.mapper.CategoryMapper;
import org.springframework.stereotype.Service;
import syh.service.DishService;
import syh.service.SetmealService;

import java.util.List;

/**
* @author shiyu
* @description 针对表【category(菜品及套餐分类)】的数据库操作Service实现
* @createDate 2023-09-01 10:34:58
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{
    @Override
    public Result pagelist(Long page, Long pageSize) {
        Page<Category> categoryPage = new Page<>();
        return Result.success(this.page(categoryPage));
    }


}





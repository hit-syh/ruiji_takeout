package syh.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import syh.dto.SetMealCategory;
import syh.pojo.Result;
import syh.pojo.Setmeal;
import syh.service.CategoryService;
import syh.service.SetmealService;
import syh.mapper.SetmealMapper;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
* @author shiyu
* @description 针对表【setmeal(套餐)】的数据库操作Service实现
* @createDate 2023-09-01 10:34:58
*/
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal>
    implements SetmealService{
    @Autowired
    CategoryService categoryService;
    @Override
    public Result listDish(Long page, Long pageSize, String name) {
        Page<Setmeal> setmealPage = new Page<>(page,pageSize);
        this.lambdaQuery().like(StringUtils.isNotBlank(name),Setmeal::getName,name).page(setmealPage);

        Page<SetMealCategory> setMealCategoryPage = new Page<>(page,pageSize);
        setMealCategoryPage.setTotal(setmealPage.getTotal());
        setMealCategoryPage.setPages(setmealPage.getPages());
        setMealCategoryPage.setRecords(
        setmealPage.getRecords().stream().map(setmeal -> {
            SetMealCategory setMealCategory = new SetMealCategory();
            BeanUtils.copyProperties(setmeal,setMealCategory);
            setMealCategory.setCategoryName(categoryService.getById(setmeal.getCategoryId()).getName());
            return setMealCategory;
        }).collect(Collectors.toList()));
        return Result.success(setMealCategoryPage);
    }
}





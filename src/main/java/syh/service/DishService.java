package syh.service;

import syh.dto.DishDto;
import syh.pojo.Dish;
import com.baomidou.mybatisplus.extension.service.IService;
import syh.pojo.Result;

/**
* @author shiyu
* @description 针对表【dish(菜品管理)】的数据库操作Service
* @createDate 2023-09-01 10:34:58
*/
public interface DishService extends IService<Dish> {

    Result addDish(DishDto dish);

    Result listDish(Long page, Long pageSize, String name);
}

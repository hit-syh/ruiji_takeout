package syh.service;

import syh.pojo.Result;
import syh.pojo.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author shiyu
* @description 针对表【setmeal(套餐)】的数据库操作Service
* @createDate 2023-09-01 10:34:58
*/
public interface SetmealService extends IService<Setmeal> {

    Result listDish(Long page, Long pageSize, String name);
}

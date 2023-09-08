package syh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import syh.pojo.DishFlavor;
import syh.service.DishFlavorService;
import syh.mapper.DishFlavorMapper;
import org.springframework.stereotype.Service;

/**
* @author shiyu
* @description 针对表【dish_flavor(菜品口味关系表)】的数据库操作Service实现
* @createDate 2023-09-01 10:34:58
*/
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor>
    implements DishFlavorService{

}





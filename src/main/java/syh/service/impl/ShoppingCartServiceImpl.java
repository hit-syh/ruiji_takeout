package syh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import syh.pojo.ShoppingCart;
import syh.service.ShoppingCartService;
import syh.mapper.ShoppingCartMapper;
import org.springframework.stereotype.Service;

/**
* @author shiyu
* @description 针对表【shopping_cart(购物车)】的数据库操作Service实现
* @createDate 2023-09-01 10:34:58
*/
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
    implements ShoppingCartService{

}





package syh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import syh.pojo.Orders;
import syh.service.OrdersService;
import syh.mapper.OrdersMapper;
import org.springframework.stereotype.Service;

/**
* @author shiyu
* @description 针对表【orders(订单表)】的数据库操作Service实现
* @createDate 2023-09-01 10:34:58
*/
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
    implements OrdersService{

}





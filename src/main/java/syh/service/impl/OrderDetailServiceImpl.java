package syh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import syh.pojo.OrderDetail;
import syh.service.OrderDetailService;
import syh.mapper.OrderDetailMapper;
import org.springframework.stereotype.Service;

/**
* @author shiyu
* @description 针对表【order_detail(订单明细表)】的数据库操作Service实现
* @createDate 2023-09-01 10:34:58
*/
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>
    implements OrderDetailService{

}





package syh.dto;

import lombok.Data;
import syh.pojo.OrderDetail;
import syh.pojo.Orders;

import java.util.List;
@Data
public class OrderDto extends Orders {
    List<OrderDetail> orderDetails;
}

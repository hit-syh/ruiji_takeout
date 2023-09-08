package syh.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.BeanDefinitionDsl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import syh.dto.OrderDto;
import syh.pojo.*;
import syh.service.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrdersService ordersService;
    @Autowired
    OrderDetailService orderDetailService;
    @Autowired
    UserService userService;
    @Autowired
    AddressBookService addressBookService;
    @Autowired
    ShoppingCartService shoppingCartService;
    @GetMapping("/page")
    public Result page(Long page, Long pageSize, Long number, @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime beginTime, @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        return Result.success(ordersService.lambdaQuery()
                .eq(number != null, Orders::getId, number)
                .ge(beginTime != null, Orders::getOrderTime, beginTime)
                .le(endTime != null, Orders::getOrderTime, endTime)
                .page(new Page<Orders>(page, pageSize))
        );
    }
    @GetMapping("/userPage")
    public Result userPage(Long page,Long pageSize,HttpSession httpSession)
    {
        Long userId=Long.valueOf(httpSession.getAttribute("user").toString());

        Page<Orders> ordersPage = new Page<>(page, pageSize);
        ordersService.lambdaQuery().eq(Orders::getUserId, userId).page(ordersPage);

        List<OrderDto> orderDtoList = ordersPage.getRecords().stream().map(orders -> {
            OrderDto orderDto = new OrderDto();
            BeanUtils.copyProperties(orders, orderDto);
            orderDto.setOrderDetails(orderDetailService.lambdaQuery().eq(OrderDetail::getOrderId, orders.getId()).list());
            return orderDto;
        }).collect(Collectors.toList());

        Page<OrderDto> orderDtoPage = new Page<>(page,pageSize);
        orderDtoPage.setRecords(orderDtoList);
        orderDtoPage.setTotal(ordersPage.getTotal());
        orderDtoPage.setPages(ordersPage.getPages());



        return Result.success(orderDtoPage);
    }
    @PostMapping("/submit")
    @Transactional
    public Result submit(@RequestBody Orders orders, HttpSession httpSession)
    {
        //加入到order
        Long userId=Long.valueOf(httpSession.getAttribute("user").toString());
        orders.setUserId(userId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        User user = userService.getById(userId);
        orders.setUserName(user.getName());
        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());
        orders.setAddress(addressBook.getDetail());
        orders.setPhone(addressBook.getPhone());
        orders.setConsignee(addressBook.getConsignee());
        ordersService.save(orders);
        Long ordersId = orders.getId();

        final BigDecimal[] count = {new BigDecimal(0)};
        //加入到orderdetils
        List<Long> shopCartIds = new ArrayList<>();
        List<ShoppingCart> shoppingCartList = shoppingCartService.lambdaQuery().eq(ShoppingCart::getUserId, userId).list();
        shoppingCartList.stream().forEach(shoppingCart -> {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(shoppingCart,orderDetail);
            orderDetail.setOrderId(ordersId);
            orderDetailService.save(orderDetail);
            shopCartIds.add(shoppingCart.getId());
             count[0] = count[0].add(shoppingCart.getAmount().multiply(new BigDecimal(shoppingCart.getNumber())));
        });
        //清空购物车
        shoppingCartService.removeByIds(shopCartIds);

        orders.setAmount(count[0]);
        orders.setNumber(ordersId.toString());
        orders.setStatus(2);
        ordersService.saveOrUpdate(orders);
        return Result.success("下单成功");
    }
    @PostMapping("/again")
    @Transactional
    public Result again(@RequestBody Map map)
    {
        Long id=Long.valueOf(map.get("id").toString());
        Orders orders = ordersService.getById(id);
        orders.setId(null);
        orders.setStatus(2);
        ordersService.save(orders);

        List<OrderDetail> orderDetails = orderDetailService.lambdaQuery().eq(OrderDetail::getOrderId, id).list();
        orderDetails.forEach(orderDetail -> {
            orderDetail.setId(null);
            orderDetail.setOrderId(orders.getId());
            orderDetailService.save(orderDetail);
        });
        return Result.success("成功");
    }
    @PutMapping
    public Result status(@RequestBody Map map)
    {
        Long id=Long.valueOf(map.get("id").toString());
        Integer status=Integer.valueOf(map.get("status").toString());
        ordersService.lambdaUpdate().eq(Orders::getId,id).set(Orders::getStatus,status).update();
        return Result.success("成功");
    }

}

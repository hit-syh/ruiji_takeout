package syh.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import syh.pojo.Result;
import syh.pojo.ShoppingCart;
import syh.service.ShoppingCartService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    ShoppingCartService shoppingCartService;
    @GetMapping("/list")
    public Result list(HttpSession httpSession){
        Long userId= (Long) httpSession.getAttribute("user");
        List<ShoppingCart> shoppingCartList = shoppingCartService.lambdaQuery().eq(ShoppingCart::getUserId, userId).list();
        return Result.success(shoppingCartList);
    }
    @PostMapping("/add")
    @Transactional
    public  Result add(@RequestBody ShoppingCart shoppingCart,HttpSession httpSession)
    {

        shoppingCart.setUserId(Long.valueOf(httpSession.getAttribute("user").toString()));


        List<ShoppingCart> shoppingCartList = shoppingCartService.lambdaQuery()
                .eq(ShoppingCart::getUserId, shoppingCart.getUserId())
                .eq(shoppingCart.getSetmealId() != null, ShoppingCart::getSetmealId, shoppingCart.getSetmealId())
                .eq(shoppingCart.getDishId() != null, ShoppingCart::getDishId, shoppingCart.getDishId())
                .eq(StringUtils.isNotBlank(shoppingCart.getDishFlavor()),ShoppingCart::getDishFlavor,shoppingCart.getDishFlavor())
                .list();
        if (shoppingCartList.size()==1)
        {
            ShoppingCart shoppingCartOne = shoppingCartList.get(0);
            shoppingCartOne.setNumber(shoppingCartOne.getNumber()+1);
            shoppingCartService.saveOrUpdate(shoppingCartOne);
        }
        else
        {
            shoppingCartService.save(shoppingCart);
        }
        return Result.success("添加成功");
    }
    @PostMapping("/sub")
    public Result sub(@RequestBody ShoppingCart shoppingCart,HttpSession httpSession)
    {
        shoppingCart.setUserId(Long.valueOf(httpSession.getAttribute("user").toString()));


        ShoppingCart shoppingCartOne = shoppingCartService.lambdaQuery()
                .eq(ShoppingCart::getUserId, shoppingCart.getUserId())
                .eq(shoppingCart.getSetmealId() != null, ShoppingCart::getSetmealId, shoppingCart.getSetmealId())
                .eq(shoppingCart.getDishId() != null, ShoppingCart::getDishId, shoppingCart.getDishId())
                .eq(StringUtils.isNotBlank(shoppingCart.getDishFlavor()),ShoppingCart::getDishFlavor,shoppingCart.getDishFlavor())
                .one();
        if (shoppingCartOne.getNumber()>=2)
        {
            shoppingCartOne.setNumber(shoppingCartOne.getNumber()-1);
            shoppingCartService.saveOrUpdate(shoppingCartOne);
        }
        else
        {
            shoppingCartService.removeById(shoppingCartOne.getId());
        }
        return Result.success("修改成功");
    }
    @DeleteMapping("/clean")
    public Result clean(HttpSession httpSession)
    {
        Long userId = Long.valueOf(httpSession.getAttribute("user").toString());
        shoppingCartService.lambdaUpdate().eq(ShoppingCart::getUserId,userId).remove();
        return Result.success("清空成功");
    }
}

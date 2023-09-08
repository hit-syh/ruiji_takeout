package syh.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import syh.exception.CodeFalseException;
import syh.pojo.Result;
import syh.pojo.User;
import syh.service.UserService;

import java.util.Map;

@RestController
@RequestMapping("/user")

public class UserController {
    @Autowired
    UserService userService;

@PostMapping("/login")
    public Result login(@RequestBody Map map, HttpSession httpSession) throws CodeFalseException {
        String phone = map.get("phone").toString();

        //判断验证码
        if(false){
            throw new CodeFalseException();
    }
        //查是不是新用户
    User user;
    if(!userService.lambdaQuery().eq(User::getPhone, phone).exists())
        {
            user = new User();
            user.setPhone(phone);
            userService.save(user);
        }
    else
    {user=userService.lambdaQuery().eq(User::getPhone, phone).one();}
        httpSession.setAttribute("user",user.getId());
        return Result.success("成功");
    }
    @PostMapping("/loginout")
    public Result logout(HttpSession httpSession)
    {
        httpSession.removeAttribute("user");
        return Result.success("Bye!");
    }
}

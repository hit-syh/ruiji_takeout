package syh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import syh.pojo.User;
import syh.service.UserService;
import syh.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author shiyu
* @description 针对表【user(用户信息)】的数据库操作Service实现
* @createDate 2023-09-01 10:34:58
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}





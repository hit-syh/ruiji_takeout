package syh.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import syh.utils.MyThreadLocal;

import java.time.LocalDateTime;
@Component
public class MyMetaObjectHanlder implements MetaObjectHandler {
    @Autowired
    HttpServletRequest httpServletRequest;
    @Override
    public void insertFill(MetaObject metaObject) {
            metaObject.setValue("createTime", LocalDateTime.now());
            metaObject.setValue("createUser", MyThreadLocal.getCurrentId());
            metaObject.setValue("updateTime", LocalDateTime.now());
            metaObject.setValue("updateUser", MyThreadLocal.getCurrentId());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", MyThreadLocal.getCurrentId());
    }
}

package syh.common;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import syh.exception.CodeFalseException;
import syh.exception.DeleteCategoryException;
import syh.exception.DeleteStatusException;
import syh.pojo.Result;

@RestControllerAdvice
public class GlobalExceptionHanlder {

    @ExceptionHandler(DeleteStatusException.class)
    public Result deleteStatusHandler(DeleteStatusException deleteException)
    {
        return Result.error("由于状态为启售，无法删除!");
    }
    @ExceptionHandler(DeleteCategoryException.class)
    public Result deleteCategoryHandler()
    {
        return Result.error("由于类别中包含菜品或套餐，无法删除！");
    }
    @ExceptionHandler(CodeFalseException.class)
    public Result codeFalseException()
    {
        return Result.error("验证码错误！");
    }
}

package syh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import syh.pojo.Employee;
import syh.pojo.Result;
import syh.service.EmployeeService;
import syh.mapper.EmployeeMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
* @author shiyu
* @description 针对表【employee(员工信息)】的数据库操作Service实现
* @createDate 2023-09-01 10:34:58
*/
@Service
@Slf4j
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
    implements EmployeeService{
    @Autowired
    private HttpServletRequest request;

    @Override
    public Result login(Employee employee) {

        QueryWrapper<Employee> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",employee.getUsername())
                .eq("password",DigestUtils.md5DigestAsHex(employee.getPassword().getBytes()));

        Employee employee1=this.getOne(queryWrapper);
        if (employee1!=null){
            if (employee1.getStatus()!=1)
                return Result.error("你已被禁用");
            request.getSession().setAttribute("employee",employee1.getId());
            return Result.success(employee1);

        }
        else
        {
            return Result.error("失败");
        }

    }

    @Override
    public Result logout() {

        request.getSession().removeAttribute("employee");
        return Result.success("成功退出");
    }

    @Override
    public Result employpage(Integer page, Integer pageSize, String name) {
        Page employeePage = new Page(page,pageSize);
        QueryWrapper<Employee> employeeQueryWrapper = new QueryWrapper<>();
        employeeQueryWrapper.like(StringUtils.isNotBlank(name),"name",name);
//        employeeQueryWrapper.select("name","username","phone","status");
//        List<Employee> employees=  this.page(employeePage,employeeQueryWrapper).getRecords();
        this.page(employeePage,employeeQueryWrapper);
        return Result.success(employeePage);
    }

    @Override
    public Result addEmploy(Employee employee) {
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        Long create_user= (Long) request.getSession().getAttribute("employee");

//        employee.setCreateUser(create_user);
//        employee.setUpdateUser(create_user);
        this.save(employee);
        return Result.success("添加成功");
    }


    @Override
    public Result employeeById(Long id) {
        return Result.success(this.getById(id));
    }

    @Override
    public Result updateEmploy(Employee employee) {
        this.saveOrUpdate(employee);
        return Result.success("修改成功");
    }

}





package syh.service;

import syh.pojo.Employee;
import com.baomidou.mybatisplus.extension.service.IService;
import syh.pojo.Result;

/**
* @author shiyu
* @description 针对表【employee(员工信息)】的数据库操作Service
* @createDate 2023-09-01 10:34:58
*/
public interface EmployeeService extends IService<Employee> {

    Result login(Employee employee);

    Result logout();

    Result employpage(Integer page, Integer pageSize, String name);

    Result addEmploy(Employee employee);
    
    Result employeeById(Long id);

    Result updateEmploy(Employee employee);
}

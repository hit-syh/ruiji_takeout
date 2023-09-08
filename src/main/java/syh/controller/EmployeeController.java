package syh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import syh.pojo.Employee;
import syh.pojo.Result;
import syh.service.EmployeeService;


@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @PostMapping("/login")
    public Result login(@RequestBody Employee employee)
    {
        return employeeService.login(employee);
    }
    @PostMapping("/logout")
    public Result logout()
    {
        return employeeService.logout();
    }
    @GetMapping("/page")
    public  Result employpage(@RequestParam(defaultValue = "1") Integer page,@RequestParam(defaultValue = "5")Integer pageSize,@RequestParam(defaultValue = "") String name)
    {
        return employeeService.employpage(page,pageSize,name);
    }
    @PostMapping("")
    public Result addEmployee(@RequestBody Employee employee)
    {
        return employeeService.addEmploy(employee);
    }
    @GetMapping("/{id}")
    public Result employeeById(@PathVariable Long id)
    {
        return employeeService.employeeById(id);
    }
    @PutMapping("")
    public Result updateEmployee(@RequestBody Employee employee)
    {
        return employeeService.updateEmploy(employee);
    }

}

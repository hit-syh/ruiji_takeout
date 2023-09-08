package syh.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import syh.pojo.AddressBook;
import syh.pojo.Result;
import syh.service.AddressBookService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/addressBook")
public class AddressBookController {
    @Autowired
    AddressBookService addressBookService;
    @GetMapping("/list")
    public Result list(HttpSession httpSession)
    {
        Long userId = Long.valueOf(httpSession.getAttribute("user").toString());
        return Result.success(addressBookService.lambdaQuery().eq(AddressBook::getUserId,userId).list());
    }
    @RequestMapping(method = {RequestMethod.PUT,RequestMethod.POST})
    public  Result add(@RequestBody AddressBook addressBook ,HttpSession httpSession)
    {
        Long userId = Long.valueOf(httpSession.getAttribute("user").toString());
        addressBook.setUserId(userId);
        addressBookService.saveOrUpdate(addressBook);
        return Result.success("添加成功");
    }
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id)
    {
        return Result.success(addressBookService.getBaseMapper().selectById(id));
    }
    @DeleteMapping
    public Result removeById( Long ids)
    {
        addressBookService.removeById(ids);
        return Result.success("删除成功");
    }
    @PutMapping("/default")
    @Transactional
    public Result setDefult(@RequestBody Map ids,HttpSession httpSession)
    {
        Long userId=Long.valueOf(httpSession.getAttribute("user").toString());
        Long id=Long.valueOf(ids.get("id").toString());
        addressBookService.lambdaUpdate().eq(AddressBook::getUserId,userId).set(AddressBook::getIsDefault,0).update();
        addressBookService.lambdaUpdate().eq(AddressBook::getId,id).set(AddressBook::getIsDefault,1).update();
        return Result.success("设置成功");
    }
    @GetMapping("/default")
    public Result getDefault(HttpSession httpSession)
    {
        Long userId=Long.valueOf(httpSession.getAttribute("user").toString());
        List<AddressBook> addressBookList = addressBookService.lambdaQuery().eq(AddressBook::getUserId, userId).eq(AddressBook::getIsDefault, 1).list();
        if (addressBookList.size()==0)
        {
            return Result.error("暂无默认收货地址");
        }
        return Result.success(addressBookList.get(0));

    }
}

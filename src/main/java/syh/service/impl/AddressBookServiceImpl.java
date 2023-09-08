package syh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import syh.pojo.AddressBook;
import syh.service.AddressBookService;
import syh.mapper.AddressBookMapper;
import org.springframework.stereotype.Service;

/**
* @author shiyu
* @description 针对表【address_book(地址管理)】的数据库操作Service实现
* @createDate 2023-09-01 10:34:58
*/
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook>
    implements AddressBookService{

}





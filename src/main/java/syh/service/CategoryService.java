package syh.service;

import syh.pojo.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import syh.pojo.Result;

/**
* @author shiyu
* @description 针对表【category(菜品及套餐分类)】的数据库操作Service
* @createDate 2023-09-01 10:34:58
*/
public interface CategoryService extends IService<Category> {

    Result pagelist(Long page, Long pageSize);

}

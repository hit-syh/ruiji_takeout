package syh.dto;

import lombok.Data;
import syh.pojo.Dish;
import syh.pojo.DishFlavor;

import java.util.List;

@Data
public class DishCategory extends Dish {
    private List<DishFlavor> flavors;
    private String categoryName;
    private Integer copies;

}

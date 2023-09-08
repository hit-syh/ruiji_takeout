package syh.dto;

import lombok.Data;
import syh.pojo.Dish;
import syh.pojo.DishFlavor;

import java.util.List;
@Data
public class DishDto extends Dish {
    private List<DishFlavor> flavors;
}

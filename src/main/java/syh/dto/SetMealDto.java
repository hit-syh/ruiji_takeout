package syh.dto;

import lombok.Data;
import syh.pojo.Setmeal;
import syh.pojo.SetmealDish;

import java.util.List;
@Data
public class SetMealDto extends Setmeal {
    private List<SetmealDish> setmealDishes;
}

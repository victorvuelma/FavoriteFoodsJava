package me.vuelma.favoritefoods.food;

import me.vuelma.favoritefoods.exception.ResourceNotFoundException;
import me.vuelma.favoritefoods.ingredient.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/foods")
public class FoodController {

    @Autowired
    private FoodRepository foodRepository;

    @GetMapping("")
    public List<Food> getAllFoods() {
        return foodRepository.findAll();
    }

    @GetMapping("/{id}")
    public Food getFoodById(@PathVariable("id") long foodId){
        return foodRepository.findById(foodId).orElseThrow(() ->
                new ResourceNotFoundException("Food", "id", foodId));
    }

    @PostMapping("")
    public Food createFood(@Valid @RequestBody Food food){
        return foodRepository.save(food);
    }

    @PutMapping("/{id}")
    public Food updateFood(@PathVariable("id") long foodId,
                           @Valid @RequestBody Food foodDetails){
        Food food = foodRepository.findById(foodId).orElseThrow(() ->
                new ResourceNotFoundException("Food", "id", foodId));

        food.setName(foodDetails.getName());
        food.setPreparationTime(foodDetails.getPreparationTime());
        food.setIngredients(foodDetails.getIngredients());

        Food updatedFood = foodRepository.save(food);
        return updatedFood;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Food> deleteIngredient(@PathVariable("id") long foodId) {
        Optional<Food> foundFood = foodRepository.findById(foodId);

        if(!foundFood.isPresent()){
            return ResponseEntity.notFound().build();
        }
        Food food = foundFood.get();

        foodRepository.delete(food);
        return ResponseEntity.noContent().build();
    }


}

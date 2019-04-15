package me.vuelma.favoritefoods.food;

import me.vuelma.favoritefoods.ingredient.Ingredient;
import me.vuelma.favoritefoods.ingredient.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/foods")
public class FoodController {

    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private IngredientRepository ingredientRepository;

    @GetMapping("")
    public ResponseEntity<List<Food>> getAllFoods() {
        List<Food> foods = foodRepository.findAll();

        return ResponseEntity.ok(foods);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Food> getFoodById(@PathVariable("id") long foodId) {
        Optional<Food> food = foodRepository.findById(foodId);

        if (!food.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(food.get());
    }

    @PostMapping("")
    public ResponseEntity<Food> createFood(@Valid @RequestBody Food food,
                                           UriComponentsBuilder ucBuilder) {
        Food createdFood = foodRepository.save(food);

        URI createdUri = ucBuilder.path("/foods/{id}").buildAndExpand(createdFood.getId()).toUri();
        return ResponseEntity.created(createdUri).body(createdFood);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Food> updateFood(@PathVariable("id") long foodId,
                                           @Valid @RequestBody Food foodDetails,
                                           UriComponentsBuilder ucBuilder) {
        Optional<Food> findFood = foodRepository.findById(foodId);

        if (!findFood.isPresent()) {
            Food createdFood = foodRepository.save(foodDetails);

            URI createdUri = ucBuilder.path("/foods/{id}").buildAndExpand(createdFood.getId()).toUri();
            return ResponseEntity.created(createdUri).body(createdFood);
        }
        Food food = findFood.get();
        food.setName(foodDetails.getName());
        food.setPreparationTime(foodDetails.getPreparationTime());
        food.setIngredients(foodDetails.getIngredients());
        food.setKitchen(foodDetails.getKitchen());

        Food updatedFood = foodRepository.save(food);
        return ResponseEntity.ok(updatedFood);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Food> deleteFood(@PathVariable("id") long foodId) {
        Optional<Food> foundFood = foodRepository.findById(foodId);

        if (!foundFood.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Food food = foundFood.get();

        foodRepository.delete(food);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{f_id}/ingredients")
    public ResponseEntity<List<Ingredient>> getIngredientsByFoodId(@PathVariable("f_id") long foodId) {
        Optional<Food> food = foodRepository.findById(foodId);

        if (!food.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(food.get().getIngredients());
    }

    @PutMapping("/{f_id}/ingredients/{i_id}")
    public ResponseEntity<List<Ingredient>> addFoodIngredient(@PathVariable("f_id") long foodId,
                                                              @PathVariable("i_id") long ingredientId) {
        Optional<Food> findFood = foodRepository.findById(foodId);
        if (!findFood.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Optional<Ingredient> findIngredient = ingredientRepository.findById(ingredientId);
        if (!findIngredient.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Food food = findFood.get();
        Ingredient ingredient = findIngredient.get();
        if (!food.getIngredients().contains(ingredient)) {
            food.getIngredients().add(ingredient);
            food = foodRepository.save(food);
        }
        return ResponseEntity.ok(food.getIngredients());
    }

    @DeleteMapping("/{f_id}/ingredients/{i_id}")
    public ResponseEntity<List<Ingredient>> removeFoodIngredient(@PathVariable("f_id") long foodId,
                                                                 @PathVariable("i_id") long ingredientId) {
        Optional<Food> findFood = foodRepository.findById(foodId);
        if (!findFood.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Optional<Ingredient> findIngredient = ingredientRepository.findById(ingredientId);
        if (!findIngredient.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Food food = findFood.get();
        Ingredient ingredient = findIngredient.get();
        if (food.getIngredients().contains(ingredient)) {
            food.getIngredients().remove(ingredient);
            food = foodRepository.save(food);
        }
        return ResponseEntity.ok(food.getIngredients());
    }

}

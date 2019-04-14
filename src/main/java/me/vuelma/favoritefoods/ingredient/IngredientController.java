package me.vuelma.favoritefoods.ingredient;

import me.vuelma.favoritefoods.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    @Autowired
    IngredientRepository ingredientRepository;

    @GetMapping("")
    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    @GetMapping("/{id}")
    public Ingredient getIngredientById(@PathVariable(value = "id") Long ingredientId) {
        return ingredientRepository.findById(ingredientId).orElseThrow(() ->
                new ResourceNotFoundException("Ingredient", "id", ingredientId));
    }

    @PostMapping("")
    public Ingredient createIngredient(@Valid @RequestBody Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }

    @PutMapping("/{id}")
    public Ingredient updateIngredient(@PathVariable(value = "id") Long ingredientId,
                                       @Valid @RequestBody Ingredient ingredientDetails) {
        Ingredient ingredient = ingredientRepository.findById(ingredientId).orElseThrow(() ->
                new ResourceNotFoundException("Ingredient", "id", ingredientId));

        ingredient.setName(ingredientDetails.getName());

        Ingredient updatedIngredient = ingredientRepository.save(ingredient);
        return updatedIngredient;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteIngredient(@PathVariable(value = "id") Long ingredientId) {
        Ingredient ingredient = ingredientRepository.findById(ingredientId).orElseThrow(() ->
                new ResourceNotFoundException("Ingredient", "id", ingredientId));

        ingredientRepository.delete(ingredient);

        return ResponseEntity.ok().build();
    }

}

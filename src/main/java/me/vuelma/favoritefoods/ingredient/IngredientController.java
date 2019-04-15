package me.vuelma.favoritefoods.ingredient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    @Autowired
    IngredientRepository ingredientRepository;

    @GetMapping("")
    public ResponseEntity<List<Ingredient>> getAllIngredients() {
        List<Ingredient> allIngredients = ingredientRepository.findAll();

        return ResponseEntity.ok(allIngredients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ingredient> getIngredientById(@PathVariable("id") long ingredientId) {
        Optional<Ingredient> ingredient = ingredientRepository.findById(ingredientId);

        if (!ingredient.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ingredient.get());
    }

    @PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Ingredient> createIngredient(@Valid @RequestBody Ingredient ingredient,
                                                       UriComponentsBuilder ucBuilder) {
        Ingredient createdIngredient = ingredientRepository.save(ingredient);

        URI createdUri = ucBuilder.path("/ingredients/{id}").buildAndExpand(createdIngredient.getId()).toUri();
        return ResponseEntity.created(createdUri).body(createdIngredient);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ingredient> updateIngredient(@PathVariable("id") long ingredientId,
                                                       @Valid @RequestBody Ingredient ingredientDetails) {
        Optional<Ingredient> findIngredient = ingredientRepository.findById(ingredientId);

        if (!findIngredient.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Ingredient ingredient = findIngredient.get();
        ingredient.setName(ingredientDetails.getName());

        Ingredient updatedIngredient = ingredientRepository.save(ingredient);
        return ResponseEntity.ok(updatedIngredient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Ingredient> deleteIngredient(@PathVariable("id") long ingredientId) {
        Optional<Ingredient> findIngredient = ingredientRepository.findById(ingredientId);

        if (!findIngredient.isPresent()) {
            return ResponseEntity.noContent().build();
        }
        Ingredient ingredient = findIngredient.get();

        ingredientRepository.delete(ingredient);
        return ResponseEntity.ok(ingredient);
    }

}

package me.vuelma.favoritefoods.kitchen;

import me.vuelma.favoritefoods.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/kitchens")
public class KitchenController {

    @Autowired
    KitchenRepository kitchenRepository;

    @GetMapping("")
    public List<Kitchen> getAllKitchens() {
        return kitchenRepository.findAll();
    }

    @GetMapping("/{id}")
    public Kitchen getKitchenById(@PathVariable("id") Long kitchenId) {
        return kitchenRepository.findById(kitchenId).orElseThrow(() ->
                new ResourceNotFoundException("Kitchen", "id", kitchenId));
    }

    @PostMapping("")
    public Kitchen createKitchen(@Valid @RequestBody Kitchen kitchen) {
        return kitchenRepository.save(kitchen);
    }

    @PutMapping("/{id}")
    public Kitchen updateKitchen(@PathVariable("id") Long kitchenId,
                                 @Valid @RequestBody Kitchen kitchenDetails) {
        Kitchen kitchen = kitchenRepository.findById(kitchenId).orElseThrow(() ->
                new ResourceNotFoundException("Kitchen", "id", kitchenId));

        kitchen.setName(kitchenDetails.getName());

        Kitchen updatedKitchen = kitchenRepository.save(kitchen);
        return updatedKitchen;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteKitchen(@PathVariable("id") Long kitchenId) {
        Kitchen kitchen = kitchenRepository.findById(kitchenId).orElseThrow(() ->
                new ResourceNotFoundException("Kitchen", "id", kitchenId));

        kitchenRepository.delete(kitchen);

        return ResponseEntity.noContent().build();
    }

}

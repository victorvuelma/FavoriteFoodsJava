package me.vuelma.favoritefoods.kitchen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/kitchens")
public class KitchenController {

    @Autowired
    KitchenRepository kitchenRepository;

    @GetMapping("")
    public ResponseEntity<List<Kitchen>> getAllKitchens() {
        List<Kitchen> kitchens = kitchenRepository.findAll();

        return ResponseEntity.ok(kitchens);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Kitchen> getKitchenById(@PathVariable("id") long kitchenId) {
        Optional<Kitchen> kitchen = kitchenRepository.findById(kitchenId);

        if (!kitchen.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(kitchen.get());
    }

    @PostMapping("")
    public ResponseEntity<Kitchen> createKitchen(@Valid @RequestBody Kitchen kitchen,
                                                 UriComponentsBuilder ucBuilder) {
        Kitchen createdKitchen = kitchenRepository.save(kitchen);

        URI createdUri = ucBuilder.path("/kitchens/{id}").buildAndExpand(createdKitchen.getId()).toUri();
        return ResponseEntity.created(createdUri).body(createdKitchen);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Kitchen> updateKitchen(@PathVariable("id") long kitchenId,
                                                 @Valid @RequestBody Kitchen kitchenDetails,
                                                 UriComponentsBuilder ucBuilder) {
        Optional<Kitchen> findKitchen = kitchenRepository.findById(kitchenId);

        if (!findKitchen.isPresent()) {
            Kitchen createdKitchen = kitchenRepository.save(kitchenDetails);

            URI createdUri = ucBuilder.path("/kitchens/{id}").buildAndExpand(createdKitchen.getId()).toUri();
            return ResponseEntity.created(createdUri).body(createdKitchen);
        }
        Kitchen kitchen = findKitchen.get();
        kitchen.setName(kitchenDetails.getName());

        Kitchen updatedKitchen = kitchenRepository.save(kitchen);
        return ResponseEntity.ok(updatedKitchen);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Kitchen> deleteKitchen(@PathVariable("id") long kitchenId) {
        Optional<Kitchen> findKitchen = kitchenRepository.findById(kitchenId);

        if (!findKitchen.isPresent()) {
            return ResponseEntity.noContent().build();
        }
        Kitchen kitchen = findKitchen.get();

        kitchenRepository.delete(kitchen);
        return ResponseEntity.ok(kitchen);
    }

}

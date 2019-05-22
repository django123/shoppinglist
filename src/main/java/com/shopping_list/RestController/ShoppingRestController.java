package com.shopping_list.RestController;


import com.shopping_list.Repository.ShoppingRepository;
import com.shopping_list.entities.Shopping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/shoppings/api")
public class ShoppingRestController {

    @Autowired
    private ShoppingRepository shoppingRepository;

    @GetMapping("/all")
    public List<Shopping> findAllShoppings(){
        return shoppingRepository.findAll();
    }

    @GetMapping("/find/{id_shop}")
    public Optional<Shopping> findShopping(@PathVariable Long id_shop){
        return shoppingRepository.findById(id_shop);
    }

    @PostMapping
    public Shopping createShopping(@RequestBody  Shopping shopping){
        return shoppingRepository.save(shopping);
    }

    @DeleteMapping("/delete/{id_shop}")
    public void deleteShoping(@PathVariable Long id_shop){
        shoppingRepository.deleteById(id_shop);
    }

    @PutMapping("/update/{id_shop}")
    public Shopping updateShopping(@RequestBody Shopping shopping, Long id_shop){

        return shoppingRepository.findById(id_shop)
                .map(shopping1 -> {
                    shopping1.setName(shopping.getName());
                    return shoppingRepository.save(shopping1);
                })
                .orElseGet(() -> {
                    shopping.setId_shop(id_shop);
                    return shoppingRepository.save(shopping);
                });
    }
}

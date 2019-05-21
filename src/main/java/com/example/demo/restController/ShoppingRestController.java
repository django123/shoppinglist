package com.example.demo.restController;

import com.example.demo.entities.Shopping;
import com.example.demo.repositiory.ShoppingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by EDOUGA on 21/05/2019.
 */

@RestController
@RequestMapping("/shoppings")
public class ShoppingRestController {

    @Autowired
    private ShoppingRepository shoppingRepository;

    @GetMapping("/all")
    private List<Shopping> findAllShoppings(){
        return shoppingRepository.findAll();
    }

    @GetMapping("/{id_shop}")
    public Shopping findShopping(@PathVariable Long id_shop){

        return shoppingRepository.getOne(id_shop);
    }

    @PostMapping("/create")
    public Shopping createShopping(@Valid @RequestBody Shopping shopping){
        return shoppingRepository.save(shopping);
    }

    @DeleteMapping("/delete/{id_shop}")
    public  void deleteShopping(@PathVariable Long id_shop){
        shoppingRepository.deleteById(id_shop);
    }

    @PutMapping("/update/{id_shop}")
    public Shopping updateShopping(@RequestBody Shopping shopping, Long id_shop){
        return shoppingRepository.findById(id_shop)
                .map(shopping1 -> {
                    shopping1.setName(shopping.getName());
                    shopping1.setComment(shopping.getComment());
                    shopping1.setDate(shopping.getDate());
                    return shoppingRepository.save(shopping1);
                })
                .orElseGet(()-> {
                    shopping.setId_shop(id_shop);
                    return shoppingRepository.save(shopping);

                });
    }
}

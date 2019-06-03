package com.shopping_list.RestController;


import com.shopping_list.Repository.ShoppingRepository;
import com.shopping_list.Repository.TaskRepository;
import com.shopping_list.entities.Shopping;
import com.shopping_list.messages.NotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/shoppings")
@Api(value="ShoppingsControllerApi", produces=MediaType.APPLICATION_JSON_VALUE)
public class ShoppingRestController {

    @Autowired
    private ShoppingRepository shoppingRepository;

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("/all")
    public List<Shopping> findAllShoppings(){
        return shoppingRepository.findAll();
    }

    @GetMapping("/find/{id_shop}")
    @ApiOperation(" gets the shopping in specific id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = Shopping.class)})
    public Optional<Shopping> findShopping(@PathVariable Long id_shop){
        return shoppingRepository.findById(id_shop);
    }

    @PostMapping
    public Shopping createShopping(@RequestBody  Shopping shopping){
        return shoppingRepository.save(shopping);
    }

    @DeleteMapping("/tasks/{task_id}/shoppings/{id_shop}")
    public String deleteShoping(@PathVariable Long id_shop , @PathVariable Long task_id){
        if(!taskRepository.existsById(task_id)) {
            throw new NotFoundException("Task not found!");
        }

        return shoppingRepository.findById(id_shop)
                .map(shopping -> {
                    shoppingRepository.delete(shopping);
                    return "Deleted Successfully!";
                }).orElseThrow(() -> new NotFoundException("Contact not found!"));

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

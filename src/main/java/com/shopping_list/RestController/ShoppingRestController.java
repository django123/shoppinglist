package com.shopping_list.RestController;


import com.shopping_list.Repository.ShoppingRepository;
import com.shopping_list.Repository.TaskRepository;
import com.shopping_list.entities.Shopping;
import com.shopping_list.entities.Task;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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


    @GetMapping
    public List<Shopping> findAllShopping(){
        List<Shopping>shoppings = shoppingRepository.findAll();
        for(Shopping shopping: shoppings){
            List<Task>tasks = new ArrayList<>();
            tasks.addAll(shopping.getTasks());
            List<Task>tasks1=new ArrayList<>();
            for (Task task:tasks){
                if (task.getStatus()==true){
                    tasks1.add(task);
                }
            }
            if ((tasks.size() != 0) && tasks.size()==tasks1.size()){
                shopping.setStatut(true);
            }
        }
        return shoppingRepository.findAll();
    }

    @GetMapping("/shopping/{shopId}")
    public Optional<Shopping> findShopping(@PathVariable Long shopId){

        return shoppingRepository.findById(shopId);
    }

    @PostMapping("/create")
    public Shopping createShopping(@RequestBody Shopping shopping){
        return shoppingRepository.save(shopping);

    }

    @PutMapping("/update/{shopId}")
    public Shopping updateShopping(@RequestBody Shopping shopping, @PathVariable Long shopId){
        shoppingRepository.findById(shopId);
        return shoppingRepository.save(shopping);

    }

    @DeleteMapping("/delete/{shopId}")
    public void deleteShopping(@PathVariable Long shopId){
       shoppingRepository.deleteById(shopId);
    }
}

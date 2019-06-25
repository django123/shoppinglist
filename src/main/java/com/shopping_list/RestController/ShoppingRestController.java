package com.shopping_list.RestController;


import com.shopping_list.Repository.ShoppingRepository;
import com.shopping_list.Repository.TaskRepository;
import com.shopping_list.Repository.UserRepository;
import com.shopping_list.entities.Shopping;
import com.shopping_list.entities.Utilisateur;
import com.shopping_list.service.MailService;
import com.shopping_list.service.ShoppingService;
import com.shopping_list.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/shoppings")
@Api(value="ShoppingsControllerApi", produces=MediaType.APPLICATION_JSON_VALUE)
public class ShoppingRestController {

    @Autowired
    private ShoppingRepository shoppingRepository;

    @Autowired
    private ShoppingService shoppingService;

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserService userService;


    @GetMapping
    public List<Shopping> findAllShopping(){
        List<Shopping>shoppings = shoppingService.findAllShopping();
        return shoppingService.findAllShopping();
    }

    @GetMapping("/shopping/{shopId}")
    public Shopping findShopping(@PathVariable Long shopId){

        return shoppingService.findShoppingId(shopId);
    }

    @PostMapping("/create")
    public Shopping createShopping(@RequestBody Shopping shopping){
        return shoppingService.createShopping(shopping);

    }

    @PutMapping("/update/{shopId}")
    public Shopping updateShopping(@RequestBody Shopping shopping, @PathVariable Long shopId){
        Shopping currentShopping = shoppingService.findShoppingId(shopId);
        currentShopping.setName(shopping.getName());
        currentShopping.setComment(shopping.getComment());
        currentShopping.setDate(shopping.getDate());

        return shoppingService.updateShopping(currentShopping);
    }

    @DeleteMapping("/delete/{shopId}")
    public void deleteShopping(@PathVariable Long shopId){
       shoppingService.deleteShopping(shopId);
    }


}

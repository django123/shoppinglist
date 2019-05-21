package com.example.demo.controller;

import com.example.demo.entities.Shopping;
import com.example.demo.repositiory.ShoppingRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

/**
 * Created by EDOUGA on 21/05/2019.
 */
@Controller
@RequestMapping("/shopping")
public class ShoppingController {


    private ShoppingRepository shoppingRepository;

    @GetMapping("/all")
    public String findAll(Model model){
        model.addAttribute("shoppings", shoppingRepository.findAll());
        return "shopping/shoppings";
    }

    @GetMapping("/form")
    public String form(Model model){
        model.addAttribute("shopping", new Shopping());
        return "shopping/form";
    }

    @PostMapping("/save")
    public String createShopping(Shopping shopping){
        shoppingRepository.save(shopping);
        return "shopping/redirection";
    }

    @GetMapping("/update/{id_shop}")
    public String updatedShopping(Model model, @PathVariable Long id_shop){
        Optional<Shopping> optional =  shoppingRepository.findById(id_shop);
        model.addAttribute("shopping", optional.get());
        return "shopping/update";
    }
    @PostMapping("/update/{id_shop}")
    public String save(Shopping shopping, @PathVariable Long id_shop, BindingResult result){

        if(result.hasErrors()) {
            shopping.setId_shop(id_shop);
            return "shopping/update";
        }
        Shopping shop = shoppingRepository.save(shopping);
        return "redirect:/shopping/detail/" +shop.getId_shop() ;
    }

    @GetMapping("/delete/{id_shop}")
    public void deleteById(@PathVariable Long id_shop, Model model) {
        Shopping shopping = shoppingRepository.findById(id_shop)
                .orElseThrow(() -> new IllegalArgumentException("Invalid shopping id:" +id_shop));
        shoppingRepository.delete(shopping);
        model.addAttribute("shoppings", shoppingRepository.findAll());
    }

    @GetMapping("/detail/{id_shop}")
    public String getShop(Model model, @PathVariable Long id_shop){
        Optional<Shopping> optional=shoppingRepository.findById(id_shop);
        model.addAttribute("shopping", optional.get());
        return "shopping/detail";

    }

    @GetMapping("/user/{user_id}")
    public String findByUser(@PathVariable Long user_id, Model model){
        List<Shopping> shoppings = shoppingRepository.findAllByUserOrderById_shopDesc(user_id);
        model.addAttribute("shoppings", shoppings);
        return "shopping/user/shoppings";
    }


}

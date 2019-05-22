package com.shopping_list.controller;

import com.shopping_list.Repository.ShoppingRepository;
import com.shopping_list.entities.Shopping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/shopping")
public class ShoppingController {

    @Autowired
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
    public String save(Shopping shopping){
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

    @GetMapping("/utilisateur/{user_id}")
    public String findByUser(@PathVariable Long user_id, Model model){
        List<Shopping> shoppings = shoppingRepository.findAllByUtilisateurOrderById_shopDesc(user_id);
        model.addAttribute("shoppings", shoppings);
        return "shopping/utilisateur/shoppings";
    }

    @GetMapping("/status/{status_id}")
    public String findByStatus(@PathVariable Long status_id, Model model){
        List<Shopping>shoppings = shoppingRepository.findAllByStatusOrderById_shopDesc(status_id);
        model.addAttribute("shoppings", shoppings);
        return "shopping/status/shoppings";
    }
}

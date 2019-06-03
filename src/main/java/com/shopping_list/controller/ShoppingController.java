package com.shopping_list.controller;

import com.shopping_list.Repository.ShoppingRepository;
import com.shopping_list.Repository.TaskRepository;
import com.shopping_list.entities.Shopping;
import com.shopping_list.entities.Task;
import com.shopping_list.messages.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Controller
public class ShoppingController {

    @Autowired
    private ShoppingRepository shoppingRepository;

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("/shopping/all")
    public String findAll(Model model){
        model.addAttribute("shoppings", shoppingRepository.findAll());
        model.addAttribute("tasks", new Task());

        return "shopping/shoppings";
    }

    @GetMapping("/shopping/form")
    public String form(Model model){
        model.addAttribute("shopping", new Shopping());
        return "shopping/form";
    }

    @PostMapping("/shopping/save")
    public String save(Shopping shopping){
        shoppingRepository.save(shopping);
        return "shopping/redirection";
    }

    @GetMapping("/shopping/update/{id_shop}")
    public String updatedShopping(Model model, @PathVariable Long id_shop){
      Optional<Shopping> optional =  shoppingRepository.findById(id_shop);
        model.addAttribute("shopping", optional.get());
        return "shopping/update";
    }
    @PostMapping("/shopping/update/{id_shop}")
    public String save(Shopping shopping, @PathVariable Long id_shop, BindingResult result){

        if(result.hasErrors()) {
            shopping.setId_shop(id_shop);
            return "shopping/update";
        }
        Shopping shop = shoppingRepository.save(shopping);
        return "redirect:/shopping/detail/" +shop.getId_shop() ;
    }

    @DeleteMapping("/task/{task_id}/shoppings/{id_shop}")
    public String deleteById(@PathVariable Long id_shop, @PathVariable Long task_id, Model model) {
        if(!taskRepository.existsById(task_id)) {
            throw new NotFoundException("Task not found with id " + task_id);
        }

        return shoppingRepository.findById(id_shop)
                .map(shopping -> {
                    shoppingRepository.delete(shopping);
                    return "redirect:/";
                }).orElseThrow(() -> new NotFoundException("shopping not found with id" + id_shop));
    }

    @GetMapping("/shopping/detail/{id_shop}")
    public String getShop(Model model, @PathVariable Long id_shop){
        Optional<Shopping> optional=shoppingRepository.findById(id_shop);
        model.addAttribute("shopping", optional.get());
        return "shopping/detail";

    }

    @GetMapping("/shopping/utilisateur/{user_id}")
    public String findByUser(@PathVariable Long user_id, Model model){
        List<Shopping> shoppings = shoppingRepository.findAllByUtilisateurOrderById_shopDesc(user_id);
        model.addAttribute("shoppings", shoppings);
        return "shopping/utilisateur/shoppings";
    }

    @GetMapping("/shopping/status/{status_id}")
    public String findByStatus(@PathVariable Long status_id, Model model){
        List<Shopping>shoppings = shoppingRepository.findAllByStatusOrderById_shopDesc(status_id);
        model.addAttribute("shoppings", shoppings);
        return "shopping/status/shoppings";
    }
}

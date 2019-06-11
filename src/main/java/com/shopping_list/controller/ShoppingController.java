package com.shopping_list.controller;

import com.shopping_list.Repository.ShoppingRepository;
import com.shopping_list.Repository.TaskRepository;
import com.shopping_list.Repository.UtilisateurRepository;
import com.shopping_list.entities.Shopping;
import com.shopping_list.entities.Task;
import com.shopping_list.entities.Utilisateur;
import com.shopping_list.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/shopping")
public class ShoppingController {

    @Autowired
    private ShoppingRepository shoppingRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @GetMapping("/all")
    public String findAll(Model model,Long shopId){
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
        model.addAttribute("shoppings", shoppingRepository.findAll());
        model.addAttribute("tasks", new Task());
        return "shopping/shoppings";
    }

    @GetMapping("/detail/{shopId}")
    public String getShop(Model model, @PathVariable Long shopId){
        Optional<Shopping> optional=shoppingRepository.findById(shopId);
        model.addAttribute("shopping", optional.get());
        return "shopping/detail";

    }

    @GetMapping("/form")
    public String form(Model model){
        model.addAttribute("shopping", new Shopping());
        return "shopping/form";
    }

    @PostMapping("/save")
    public String save(Shopping shopping, HttpSession session){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Utilisateur user = utilisateurRepository.findByEmail(auth.getName());
        shoppingRepository.save(shopping);
        MailService backMessage = new MailService();
        backMessage.sendSimpleMessage(
                "edougajean@gmail.com",
                "Notification de l'ajout d'un shopping, Titre : "+ shopping.getName(),
               user.getName() + " vous notifi qu'il vous a partager sa liste de course :  veuillez bien prendre connaissance de cette dernière " +shopping.getName()
        );
        return "shopping/redirection";
    }

    @GetMapping("/update/{shopId}")
    public String updatedShopping(Model model, @PathVariable Long shopId){
      Optional<Shopping> optional =  shoppingRepository.findById(shopId);
        model.addAttribute("shopping", optional.get());
        return "shopping/update";
    }
    @PostMapping("/update/{shopId}")
    public String save(Shopping shopping, @PathVariable Long shopId, BindingResult result){

        if(result.hasErrors()) {
            shopping.setShopId(shopId);
            return "shopping/update";
        }
        Shopping shop = shoppingRepository.save(shopping);
        return "redirect:/shopping/detail/" +shop.getShopId() ;
    }

    @GetMapping("/delete/{shopId}")
    public String deleteById(@PathVariable Long shopId, Model model) {
        Shopping shopping = shoppingRepository.findById(shopId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid shopping id:" +shopId));
        shoppingRepository.delete(shopping);
        model.addAttribute("shoppings", shoppingRepository.findAll());
        return "redirect:/";

    }


    @GetMapping("/utilisateur/{userId}")
    public String findByUser(@PathVariable Long userId, Model model){
        List<Shopping> shoppings = shoppingRepository.findAllByUtilisateurOrderByShopIdDesc(userId);
        model.addAttribute("shoppings", shoppings);
        return "shopping/utilisateur/shoppings";
    }

}

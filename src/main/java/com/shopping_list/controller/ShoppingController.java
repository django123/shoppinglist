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
import java.util.Collection;
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
    public String findAll(Model model){
        List<Shopping>shoppings = shoppingRepository.findByArchived(false);
        for(Shopping shopping: shoppings){
            List<Task>tasks = new ArrayList<>();
            tasks.addAll(shopping.getTasks());
            List<Task>tasks1=new ArrayList<>();
            for (Task task:tasks){
                if (task.getStatus()!=null && task.getStatus()){
                    tasks1.add(task);
                }
            }
            if ((tasks.size() != 0) && tasks.size()==tasks1.size()){
                shopping.setStatut(true);
            }
        }
        List<Integer> numbers= new ArrayList<>();
        for (Shopping shopping : shoppings){
            Collection<Task> tasks= shopping.getTasks();
            Collection<Task> tasks1=new ArrayList<>();

            for (Task task:tasks){
                if (task.getStatus() != null  && task.getStatus() == true){
                    tasks1.add(task);
                }
            }
            int nombre = tasks1.size();
            numbers.add(nombre);
            System.out.println(numbers);
        }

        model.addAttribute("tasks_done",numbers);
        model.addAttribute("utilisateur", utilisateurRepository.findAll());
        model.addAttribute("shoppings", shoppings);
        model.addAttribute("tasks", new Task());
        return "shopping/shoppings";
    }

    @GetMapping("/detail/{shopId}")
    public String getShop(Model model, @PathVariable Long shopId, HttpSession session){
        Optional<Shopping> optional=shoppingRepository.findById(shopId);
        session.setAttribute("shopId", optional.get().getShopId());
        model.addAttribute("shopping", optional.get());
        return "shopping/detail";

    }

    @GetMapping("/form")
    public String form(Model model){
        model.addAttribute("shopping", new Shopping());
        return "shopping/form";
    }

    @PostMapping("/save")
    public String save(Shopping shopping){
        shopping.setArchived(false);
        shoppingRepository.save(shopping);
        return "shopping/redirection";
    }

    @PostMapping("/email/user")
    public String shareMail(Shopping shopping,Long id, String nom, String commentaire,  HttpSession session, String userId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Utilisateur user = utilisateurRepository.getOne(Long.parseLong(userId));
        shopping.setShopId(id);
        shopping.setName(nom);
        shopping.setComment(commentaire);
        //shopping.setDate(dte);
        shoppingRepository.save(shopping);
        MailService backMessage = new MailService();
        backMessage.sendSimpleMessage(
                user.getEmail(),
                "Notification of the addition of a shopping, Title : "+ shopping.getName(),
                user.getName() + " notifies you that he has shared his shopping list with you : " + "\n"+
                        " please read this last one entitled:  " +"\n"
                        +shopping.getName()
        );
        return "email";

    }

    @GetMapping("/update/{shopId}")
    public String updatedShopping(Model model, @PathVariable Long shopId){
        Shopping shopping =  shoppingRepository.findById(shopId).get();
        model.addAttribute("shopping", shopping);
        return "shopping/edit";
    }
    @PostMapping("/update/{shopId}")
    public String save(Shopping shopping, @PathVariable Long shopId, BindingResult result,
                       String name, String comment, String archived){

        if(result.hasErrors()) {
            shopping.setShopId(shopId);
            return "shopping/edit";
        }
        shopping.setName(name);
        shopping.setComment(comment);
        shopping.setArchived(Boolean.parseBoolean(archived));
        Shopping shop = shoppingRepository.save(shopping);
        return "redirect:/shopping/detail/" +shop.getShopId() ;
    }

    @GetMapping("/delete/{shopId}")
    public String deleteById(@PathVariable Long shopId, Model model) {
        Shopping shopping = shoppingRepository.findById(shopId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid shopping id:" +shopId));
        shoppingRepository.delete(shopping);
        model.addAttribute("shoppings", shoppingRepository.findAll());
        return "redirect:/shopping/all";

    }

    @GetMapping("/archived/{shopId}")
    public String archived(@PathVariable Long shopId){
        List<Shopping> shoppings = shoppingRepository.findByArchived(true);
        Shopping shopping = shoppingRepository.getOne(shopId);
        if ( shopping.getArchived() == true){
            shopping.setArchived(false);
        }else {
            shopping.setArchived(true);
        }
        shoppingRepository.save(shopping);
        return "redirect:/shopping/all";
    }

    @GetMapping("/archive")
    public String findAllArchive(Model model){
        List<Shopping>shoppings = shoppingRepository.findByArchived(true);
        model.addAttribute("shoppings", shoppings);
        return "shopping/archive";
    }



}

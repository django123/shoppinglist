package com.shopping_list.controller;

import com.shopping_list.Repository.ShareRepository;
import com.shopping_list.Repository.ShoppingRepository;
import com.shopping_list.Repository.TaskRepository;
import com.shopping_list.Repository.UserRepository;
import com.shopping_list.entities.Share;
import com.shopping_list.entities.Shopping;
import com.shopping_list.entities.Task;
import com.shopping_list.entities.Utilisateur;
import com.shopping_list.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.*;


@Controller
@RequestMapping("/shopping")
public class ShoppingController {



    @Autowired
    private ShoppingRepository shoppingRepository;

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ShareRepository shareRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/all")
    public String findAll(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Utilisateur user = userService.findUserByEmail(auth.getName());
        List<Shopping>shoppings1 = shoppingRepository.findByArchived(false);
        List<Shopping>shoppings2 = shoppingRepository.findByUtilisateurs_UserId(user.getUserId());
        List<Shopping> shoppings= new ArrayList<>();
        for (Shopping shopping : shoppings1){
            for (int i=0; i<shoppings2.size(); i++){
                if (shopping.getShopId().equals(shoppings2.get(i).getShopId())){
                    shoppings.add(shoppings2.get(i));

                }
            }

        }
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
            System.out.println(shoppings);
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
        }

        model.addAttribute("tasks_done",numbers);
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("user", user);
        model.addAttribute("shoppings", shoppings);

        model.addAttribute("tasks", new Task());
        return "shopping/shoppings";
    }

    @GetMapping("/detail/{shopId}")
    public String getShop(Model model, @PathVariable Long shopId, HttpSession session){
        Optional<Shopping> optional=shoppingRepository.findById(shopId);
        session.setAttribute("shopId", optional.get().getShopId());
        model.addAttribute("shopping", optional.get());
        model.addAttribute("users",userRepository.findAll());
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
        Utilisateur user = userService.findUserByEmail(auth.getName());
        shopping.setUtilisateurs(new HashSet<Utilisateur>(Arrays.asList(user)));
        shopping.setSaverName(user.getName());
        shopping.setArchived(false);
        shopping.setStatut(false);
        shopping.setShared(false);
        shoppingRepository.save(shopping);
        return "redirect:/shopping/all";
    }

    @GetMapping("/shared")
    public String sharedShopping(Model model, HttpSession session){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Utilisateur user = userService.findUserByEmail(auth.getName());
        List<Shopping>shoppings1 = shoppingRepository.findByShared(true);
        List<Shopping>shoppings2 = shoppingRepository.findByUtilisateurs_UserId(user.getUserId());
        List<Shopping> shoppings3= new ArrayList<>();
        for (Shopping shopping: shoppings1){
            for (int i=0; i<shoppings2.size(); i++){
                if (shopping.getShopId().equals(shoppings2.get(i).getShopId())){
                    shoppings3.add(shoppings2.get(i));

                }
            }

        }
        model.addAttribute("shared", shoppings3);
        return "shopping/shared";
    }

    @PostMapping("/share/user")
    public String shareShopping(Share share, String userId, String shopId){
        Utilisateur user = userRepository.getOne(Long.parseLong(userId));
        Shopping shopping = shoppingRepository.getOne(Long.parseLong(shopId));
        shopping.add(user);
        shopping.setShared(true);
        share.setUserId(user.getUserId());
        share.setShopId(shopping.getShopId());
        shoppingRepository.save(shopping);
        shareRepository.save(share);
        return "redirect:/shopping/all";

    }

    @GetMapping("/update/{shopId}")
    public String updatedShopping(Model model, @PathVariable Long shopId){
        Shopping shopping =  shoppingRepository.findById(shopId).get();
        model.addAttribute("shopping", shopping);
        return "shopping/edit";
    }
    @PostMapping("/update/{shopId}")
    public String save(@Valid Shopping shopping, @PathVariable("shopId") Long shopId, BindingResult result,
                       String name, String comment, String archived, String statut, String saverName,
                       String shared, Model model){


        if(result.hasErrors()) {
            shopping.setShopId(shopId);
            return "shopping/edit";
        }
        shopping.setName(name);
        shopping.setComment(comment);
        shopping.setArchived(Boolean.parseBoolean(archived));
        shopping.setShared(Boolean.parseBoolean(shared));
        shopping.setStatut(Boolean.parseBoolean(statut));
        shopping.setSaverName(saverName);
        shoppingRepository.save(shopping);
        model.addAttribute("shoppings", shoppingRepository.findAll());
        return "redirect:/shopping/all";
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Utilisateur user = userService.findUserByEmail(auth.getName());
        List<Shopping>shoppings = shoppingRepository.findByArchived(true);
        List<Shopping>shoppings2 = shoppingRepository.findByUtilisateurs_UserId(user.getUserId());
        List<Shopping>shoppings1 = new ArrayList<>();
        for(Shopping shopping : shoppings){
            for (int i=0; i<shoppings2.size(); i++ ){
                if (shopping.getShopId().equals(shoppings2.get(i).getShopId())){
                    shoppings1.add(shopping);
                }
            }
        }
        model.addAttribute("shoppings", shoppings1);
        return "shopping/archive";
    }



}

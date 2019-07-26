package com.shopping_list.controller;

import com.shopping_list.Repository.ShoppingRepository;
import com.shopping_list.Repository.TaskRepository;
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
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/task")
public class TaskController {


    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ShoppingRepository shoppingRepository;

    @GetMapping("/all")
    public String findAll(Model model){
        model.addAttribute("tasks", taskRepository.findAll());
        return "task/tasks";
    }

    @GetMapping("/form")
    public String form(Model model, Long shopId){
        List<Shopping>shoppings=shoppingRepository.findAll();
        model.addAttribute("task", new Task());
        model.addAttribute("shoppings", shoppings);
        return "tasks/new";
    }

    @PostMapping("/save")
    public String save(@Valid Task task, Long shopId, HttpSession session){
        Shopping shopping = shoppingRepository.findById(shopId).get();
        task.setStatus(false);
        task.setShopping(shopping);
        session.setAttribute("shopId",shopId);
        taskRepository.save(task);
        return "redirect:/shopping/detail/" +session.getAttribute("shopId") ;
    }

    @GetMapping("/update/{taskId}")
    public String updatedTask(Model model, @PathVariable Long taskId){
        Task task =  taskRepository.findById(taskId).get();
        model.addAttribute("task", task);
        return "tasks/edit";
    }

    @PostMapping("/update/{taskId}")
    public String save(@Valid Task task, @PathVariable("taskId") Long taskId,
                       BindingResult result, Model model, HttpSession session, String name, String description, String status ){
        Shopping shopping = shoppingRepository.getOne((Long)session.getAttribute("shopId"));
        task.setName(name);
        task.setDescription(description);
        task.setStatus(Boolean.parseBoolean(status));
        task.setShopping(shopping);
        taskRepository.save(task);
        model.addAttribute("tasks", taskRepository.findAll());
        return "redirect:/shopping/detail/" +shopping.getShopId() ;

    }

    @GetMapping("/delete/{taskId}")
    public String deleteById(@PathVariable Long taskId, Model model, HttpSession session) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid shopping id:" +taskId));
        System.out.println("task id: " + task.getTaskId());
        Shopping shopping = shoppingRepository.getOne((Long)session.getAttribute("shopId"));
        taskRepository.delete(task);
        model.addAttribute("tasks", taskRepository.findAll());
        return "redirect:/shopping/detail/"+shopping.getShopId() ;
    }

    @GetMapping("/detail/{taskId}")
    public String getTask(Model model, @PathVariable Long taskId){
        Task task=taskRepository.findById(taskId).get();
        model.addAttribute("task", task);
        return "task/detail";

    }

    @GetMapping("/user/{userId}")
    public String findByUser(@PathVariable Long userId, Model model){
        List<Task> tasks = taskRepository.findAllByShoppingOrderByTaskIdDesc(userId);
        model.addAttribute("tasks", tasks);
        return "task/user/tasks";
    }
    @GetMapping("/active/{taskId}")
    public String active(@PathVariable Long taskId, HttpSession session){
        Task task = taskRepository.getOne(taskId);
        if (task.getStatus()== true){
            task.setStatus(false);
        }else {
            task.setStatus(true);
        }
        Shopping shopping = shoppingRepository.getOne((Long)session.getAttribute("shopId"));
        taskRepository.save(task);
        return "redirect:/shopping/detail/"+shopping.getShopId() ;
    }

}

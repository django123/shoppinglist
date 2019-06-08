package com.shopping_list.controller;

import com.shopping_list.Repository.ShoppingRepository;
import com.shopping_list.Repository.TaskRepository;
import com.shopping_list.entities.Shopping;
import com.shopping_list.entities.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    public String form(Model model){
        model.addAttribute("task", new Task());
//        model.addAttribute("shopping", shoppingRepository.findById(id_shop));
        return "task/form";
    }

    @PostMapping("/save")
    public String save(@Valid Task task, Long shopId){
        System.out.println(shopId);
        Shopping shopping = shoppingRepository.getOne(shopId);
        System.out.println(shopping);
        task.setStatus(false);
        task.setShopping(shopping);
        taskRepository.save(task);
        return "task/redirection";
    }

    @GetMapping("/update/{taskId}")
    public String updatedTask(Model model, @PathVariable Long taskId){
        Optional<Task> optional =  taskRepository.findById(taskId);
        model.addAttribute("task", optional.get());
        return "task/update";
    }
    @PostMapping("/update/{taskId}")
    public String save(@Valid Task task, @PathVariable("taskId") Long taskId,
                       BindingResult result, Model model, Long shopId){
        if (result.hasErrors()) {
            task.setTaskId(taskId);
            return "task/update";
        }
        Shopping shopping = shoppingRepository.getOne(shopId);
        task.setShopping(shopping);
        taskRepository.save(task);
        model.addAttribute("tasks", taskRepository.findAll());
        return "redirect:/shopping/all";

    }

    @GetMapping("/delete/{taskId}")
    public String deleteById(@PathVariable Long taskId, Model model) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid shopping id:" +taskId));
        System.out.println("task id: " + task.getTaskId());
        taskRepository.delete(task);
        model.addAttribute("tasks", taskRepository.findAll());
        return "redirect:/shopping/all";
    }

    @GetMapping("/detail/{taskId}")
    public String getTask(Model model, @PathVariable Long taskId){
        Optional<Task> optional=taskRepository.findById(taskId);
        model.addAttribute("task", optional.get());
        return "task/detail";

    }

    @GetMapping("/utilisateur/{userId}")
    public String findByUser(@PathVariable Long userId, Model model){
        List<Task> tasks = taskRepository.findAllByShoppingOrderByTaskIdDesc(userId);
        model.addAttribute("tasks", tasks);
        return "task/utilisateur/tasks";
    }
    @GetMapping("/active/{taskId}")
    public String active(@PathVariable Long taskId){
        Task task = taskRepository.getOne(taskId);
        if (task.getStatus()== true){
            task.setStatus(false);
        }else {
            task.setStatus(true);
        }
        taskRepository.save(task);
        return "redirect:/shopping/all";
    }
}

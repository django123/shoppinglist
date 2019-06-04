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
    public String save(@Valid Task task, Long id_shop){
        System.out.println(id_shop);
        Shopping shopping = shoppingRepository.getOne(id_shop);
        System.out.println(shopping);
        task.setShopping(shopping);
        taskRepository.save(task);
        return "task/redirection";
    }

    @GetMapping("/update/{task_id}")
    public String updatedTask(Model model, @PathVariable Long task_id){
        Optional<Task> optional =  taskRepository.findById(task_id);
        model.addAttribute("task", optional.get());
        return "task/update";
    }
    @PostMapping("/update/{task_id}")
    public String save(@Valid Task task, @PathVariable("task_id") Long task_id,
                       BindingResult result, Model model, Long id_shop){

        if (result.hasErrors()) {
            task.setTask_id(task_id);
            return "task/update";
        }
        Shopping shopping = shoppingRepository.getOne(id_shop);
        task.setShopping(shopping);
        taskRepository.save(task);
        model.addAttribute("tasks", taskRepository.findAll());
        return "redirect:/shopping/all";

/*        if(result.hasErrors()) {
            task.setTask_id(task_id);
            return "task/update";
        }
       System.out.println(id_shop);
        Shopping shopping = shoppingRepository.getOne(id_shop);
        task.setShopping(shopping);
        Task task1 = taskRepository.save(task);
        return "redirect:/task/detail/" +task1.getTask_id() ;*/
    }

    @GetMapping("/delete/{task_id}")
    public String deleteById(@PathVariable Long task_id, Model model) {
        Task task = taskRepository.findById(task_id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid shopping id:" +task_id));
        System.out.println("task id: " + task.getTask_id());
        taskRepository.delete(task);
        model.addAttribute("tasks", taskRepository.findAll());
        return "redirect:/shopping/all";
    }

    @GetMapping("/detail/{task_id}")
    public String getTask(Model model, @PathVariable Long task_id){
        Optional<Task> optional=taskRepository.findById(task_id);
        model.addAttribute("task", optional.get());
        return "task/detail";

    }

    @GetMapping("/utilisateur/{user_id}")
    public String findByUser(@PathVariable Long user_id, Model model){
        List<Task> tasks = taskRepository.findAllByShoppingOrderByTask_idDesc(user_id);
        model.addAttribute("tasks", tasks);
        return "task/utilisateur/tasks";
    }
}

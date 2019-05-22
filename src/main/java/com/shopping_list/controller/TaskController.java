package com.shopping_list.controller;

import com.shopping_list.Repository.TaskRepository;
import com.shopping_list.entities.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("/all")
    public String findAll(Model model){
        model.addAttribute("tasks", taskRepository.findAll());
        return "task/tasks";
    }

    @GetMapping("/form")
    public String form(Model model){
        model.addAttribute("task", new Task());
        return "task/form";
    }

    @PostMapping("/save")
    public String save(Task task){
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
    public String save(Task task, @PathVariable Long task_id, BindingResult result){

        if(result.hasErrors()) {
            task.setTask_id(task_id);
            return "task/update";
        }
        Task task1 = taskRepository.save(task);
        return "redirect:/task/detail/" +task1.getTask_id() ;
    }

    @GetMapping("/delete/{task_id}")
    public void deleteById(@PathVariable Long task_id, Model model) {
        Task shopping = taskRepository.findById(task_id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid shopping id:" +task_id));
        taskRepository.delete(shopping);
        model.addAttribute("tasks", taskRepository.findAll());
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

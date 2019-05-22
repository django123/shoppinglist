package com.shopping_list.RestController;

import com.shopping_list.Repository.TaskRepository;
import com.shopping_list.entities.Shopping;
import com.shopping_list.entities.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/task/api")
public class TaskRestController {


    @Autowired
    private TaskRepository taskRepository;

    @GetMapping
    public List<Task>findShoppings(){
       return  taskRepository.findAll();
    }

    @GetMapping("/find/{task_id)")
    public Optional<Task> findShoppingById(@PathVariable Long task_id){
        return taskRepository.findById(task_id);
    }

    @PostMapping("/create")
    public Task createTask(@RequestBody Task task){
        return taskRepository.save(task);
    }

    @DeleteMapping("/delete/{task_id}")
    public void deleteShopping(@PathVariable Long task_id){
        taskRepository.deleteById(task_id);
    }

    @PutMapping("/update/{task_id}")
    public Task updateShopping(@RequestBody Task task, Long task_id){

        return taskRepository.findById(task_id)
                .map(task1 -> {
                    task1.setName(task.getName());
                    return taskRepository.save(task1);
                })
                .orElseGet(() -> {
                    task.setTask_id(task_id);
                    return taskRepository.save(task);
                });
    }
}

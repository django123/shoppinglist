package com.shopping_list.RestController;

import com.shopping_list.Repository.ShoppingRepository;
import com.shopping_list.Repository.TaskRepository;
import com.shopping_list.entities.Shopping;
import com.shopping_list.entities.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TaskRestController {


    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ShoppingRepository shoppingRepository;

    @GetMapping
    public List<Task>findTasks(){
       return  taskRepository.findAll();
    }

    @GetMapping("/find/{task_id)")
    public Optional<Task> findTaskById(@PathVariable Long task_id){
        return taskRepository.findById(task_id);
    }

    @PostMapping("/create")
    public Task createTask(@RequestBody Task task, Long id_shop){
        Shopping shopping = shoppingRepository.getOne(id_shop);
        task.setStatus(false);
        task.setShopping(shopping);
        return taskRepository.save(task);
    }

    @DeleteMapping("/delete/{task_id}")
    public void deleteTask(@PathVariable Long task_id){
        taskRepository.deleteById(task_id);
    }

    @PutMapping("/update/{task_id}")
    public Task updateTask(@RequestBody Task task, Long task_id,Long id_shop){

        return taskRepository.findById(task_id)
                .map(task1 -> {
                    task1.setName(task.getName());
                    return taskRepository.save(task1);
                })
                .orElseGet(() -> {
                    task.setTask_id(task_id);
                    Shopping shopping = shoppingRepository.getOne(id_shop);
                    task.setShopping(shopping);
                    return taskRepository.save(task);
                });
    }
}

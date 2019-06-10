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

    @GetMapping("/find/{taskId)")
    public Optional<Task> findTaskById(@PathVariable Long taskId){
        return taskRepository.findById(taskId);
    }

    @PostMapping("/create")
    public Task createTask(@RequestBody Task task, Long shopId){
        Shopping shopping = shoppingRepository.getOne(shopId);
        task.setStatus(false);
        task.setShopping(shopping);
        return taskRepository.save(task);
    }

    @DeleteMapping("/delete/{taskId}")
    public void deleteTask(@PathVariable Long taskId){
        taskRepository.deleteById(taskId);
    }

    @PutMapping("/update/{taskId}")
    public Task updateTask(@RequestBody Task task, Long taskId,Long shopId){

        return taskRepository.findById(taskId)
                .map(task1 -> {
                    task1.setName(task.getName());
                    return taskRepository.save(task1);
                })
                .orElseGet(() -> {
                    task.setTaskId(taskId);
                    Shopping shopping = shoppingRepository.getOne(shopId);
                    task.setShopping(shopping);
                    return taskRepository.save(task);
                });
    }

    @GetMapping("/active/{taskId}")
    public Task activeTask(@PathVariable Long taskId){
        Task task = taskRepository.getOne(taskId);
        if (task.getStatus()== true){
            task.setStatus(false);
        }else {
            task.setStatus(true);
        }
       return taskRepository.save(task);

    }
}

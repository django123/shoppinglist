package com.shopping_list.RestController;

import com.shopping_list.Repository.ShoppingRepository;
import com.shopping_list.Repository.TaskRepository;
import com.shopping_list.entities.Shopping;
import com.shopping_list.entities.Task;
import com.shopping_list.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskRestController {


    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ShoppingRepository shoppingRepository;

    @GetMapping
    public List<Task>findTasks(){
       return  taskService.findAllTask();
    }

    @GetMapping("/find/{taskId)")
    public Task findTaskById(@PathVariable Long taskId){
        return taskService.findTaskId(taskId);
    }

    @PostMapping("/create")
    public Task createTask(@RequestBody Task task, Long shopId){
        Shopping shopping = shoppingRepository.getOne(shopId);
        task.setStatus(false);
        task.setShopping(shopping);
        return taskService.createTask(task);
    }

    @DeleteMapping("/delete/{taskId}")
    public void deleteTask(@PathVariable Long taskId){
        taskService.deleteTask(taskId);
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

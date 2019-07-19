package com.shopping_list.RestController;

import com.shopping_list.Repository.ShoppingRepository;
import com.shopping_list.Repository.TaskRepository;
import com.shopping_list.entities.Shopping;
import com.shopping_list.entities.Task;
import com.shopping_list.messages.NotFoundException;
import com.shopping_list.service.ShoppingService;
import com.shopping_list.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    @Autowired
    private ShoppingService shoppingService;

    @GetMapping
    public List<Task>findTasks(){
       return  taskService.findAllTask();
    }

    @GetMapping("/find/{taskId)")
    public Task findTaskById(@PathVariable Long taskId){
        return taskService.findTaskId(taskId);
    }

    @RequestMapping(value = "/{shopId}/task",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Task createTask(@Valid @RequestBody Task task, @PathVariable Long shopId){
        return shoppingRepository.findById(shopId)
                .map(shopping -> {
                    task.setShopping(shopping);
                    return taskRepository.save(task);
                }).orElseThrow(() -> new NotFoundException("Shopping not found!"));
    }

    @DeleteMapping("/delete/{taskId}")
    public void deleteTask(@PathVariable Long taskId){
        taskService.deleteTask(taskId);
    }

    @RequestMapping(value = "/update/{taskId}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Task updateTask(@Valid @RequestBody Task task, @PathVariable Long taskId){

        Task  task1 = taskService.findTaskId(taskId);
        if (task.getName() != null)
            task1.setName(task.getName());
        if (task.getStatus() != null)
            task1.setStatus(task.getStatus());
        if (task.getDescription() != null)
            task1.setDescription(task.getDescription());
        taskService.updateTask(task1);
        return task;
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

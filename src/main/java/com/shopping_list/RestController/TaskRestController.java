package com.shopping_list.RestController;


import com.shopping_list.Repository.ShoppingRepository;
import com.shopping_list.Repository.TaskRepository;
import com.shopping_list.entities.Shopping;
import com.shopping_list.entities.Task;
import com.shopping_list.messages.NotFoundException;
import com.shopping_list.service.ShoppingService;
import com.shopping_list.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;


@Api(description = "Gestion des tâches")
@RestController
public class TaskRestController {
    private final Logger log = LoggerFactory.getLogger(TaskRestController.class);
    private static final String ENTITY_NAME = "task";
    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ShoppingRepository shoppingRepository;

    
    @GetMapping("/tasks")
    public List<Task> findTasks(){
        return  taskService.findAllTask();
    }


    @GetMapping("/tasks/{taskId}")
    public Task findTaskById(@PathVariable Long taskId){
        return taskService.findTaskId(taskId);
    }


   @PostMapping("/tasks/create/{shopId}/task")
    public Task createTask(@Valid @RequestBody Task task, @PathVariable Long shopId){

        return shoppingRepository.findById(shopId)
                .map(shopping -> {
                    task.setStatus(false);
                    task.setShopping(shopping);
                    return taskRepository.save(task);
                }).orElseThrow(() -> new NotFoundException("Shopping not found!"));
    }


    @PutMapping("/tasks/update/{taskId}")
    public Task updateTask(@Valid @RequestBody Task task, @PathVariable Long taskId,HttpSession session){

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
    @DeleteMapping("/tasks/{taskId}")
    public void deleteTask(@PathVariable Long taskId){
        taskService.deleteTask(taskId);
    }


    @GetMapping("/tasks/active/{taskId}")
    public ResponseEntity<Object> activeTask(@PathVariable Long taskId) throws URISyntaxException {
        Task task = taskRepository.findById(taskId).get();
        if (task.getStatus()== true){
            task.setStatus(false);
        }else {
            task.setStatus(true);
        }
        taskRepository.save(task);
        return new ResponseEntity<>(task, null, HttpStatus.OK);

    }
}

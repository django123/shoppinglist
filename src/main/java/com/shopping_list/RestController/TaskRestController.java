package com.shopping_list.RestController;

import com.shopping_list.Repository.ShoppingRepository;
import com.shopping_list.Repository.TaskRepository;
import com.shopping_list.entities.Shopping;
import com.shopping_list.entities.Task;
import com.shopping_list.exception.BadRequestAlertException;
import com.shopping_list.exception.HeaderUtil;
import com.shopping_list.messages.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.shopping_list.service.ShoppingService;
import com.shopping_list.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

@RestController
@RequestMapping("/api/tasks")
public class TaskRestController {

    private final Logger log = LoggerFactory.getLogger(TaskRestController.class);
    private static final String ENTITY_NAME = "task";
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


    @DeleteMapping("/{taskId}")
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

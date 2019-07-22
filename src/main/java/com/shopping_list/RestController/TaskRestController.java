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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.util.List;



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

    @RequestMapping(value = "/update/{taskId}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Task updateTask(@Valid @RequestBody Task task, @PathVariable Long taskId,HttpSession session){

        Task  task1 = taskService.findTaskId(taskId);
        Shopping shopping = shoppingRepository.getOne((Long)session.getAttribute("shopId"));
        if (task.getName() != null)
            task1.setName(task.getName());
        if (task.getStatus() != null)
            task1.setStatus(task.getStatus());
        if (task.getDescription() != null)
            task1.setDescription(task.getDescription());
        if( task.getShopping() != null)
            task1.setShopping(shopping);
        taskService.updateTask(task1);
        return task;
    }

    @RequestMapping(value = "/active/{taskId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void activeTask(@PathVariable Long taskId){
        Task task = taskRepository.getOne(taskId);

        if (task.getStatus()== true){
            task.setStatus(false);
        }else {
            task.setStatus(true);
        }
       taskRepository.save(task);

    }

    public List<Object> active(@PathVariable Long taskId, HttpSession session){
        Task task = taskRepository.getOne(taskId);
        if (task.getStatus()== true){
            task.setStatus(false);
        }else {
            task.setStatus(true);
        }
        taskRepository.save(task);

        return null;
    }
}

package com.shopping_list.RestController;


import com.shopping_list.Repository.ShoppingRepository;
import com.shopping_list.Repository.TaskRepository;
import com.shopping_list.entities.Shopping;
import com.shopping_list.entities.Task;
import com.shopping_list.messages.NotFoundException;
import com.shopping_list.service.ShoppingService;
import com.shopping_list.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.util.List;


@CrossOrigin
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

    @GetMapping("/tasks")
    public List<Task> findTasks(){
        return  taskService.findAllTask();
    }

    @GetMapping("/tasks/{taskId}")
    public Task findTaskById(@PathVariable Long taskId){
        return taskService.findTaskId(taskId);
    }
    @PostMapping("/tasks/{shopId}/task")
    public Task createTask(@Valid @RequestBody Task task, @PathVariable Long shopId){

        return shoppingRepository.findById(shopId)
                .map(shopping -> {
                    task.setShopping(shopping);
                    return taskRepository.save(task);
                }).orElseThrow(() -> new NotFoundException("Shopping not found!"));
    }
    @PutMapping("/tasks/update/{taskId}")
    public Task updateTask(@Valid @RequestBody Task task, @PathVariable Long taskId,HttpSession session){

        Task  task1 = taskService.findTaskId(taskId);
//        Shopping shopping = shoppingRepository.getOne((Long)session.getAttribute("shopId"));
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
    public Task activeTask(@PathVariable Long taskId, HttpSession session){
        Task task = taskRepository.getOne(taskId);
        if (task.getStatus()== true){
            task.setStatus(false);
        }else {
            task.setStatus(true);
        }
        Shopping shopping = shoppingRepository.getOne((Long)session.getAttribute("shopId"));
        return taskRepository.save(task);

    }
}

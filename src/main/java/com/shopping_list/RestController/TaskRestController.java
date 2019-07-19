package com.shopping_list.RestController;

import com.shopping_list.Repository.ShoppingRepository;
import com.shopping_list.Repository.TaskRepository;
import com.shopping_list.entities.Shopping;
import com.shopping_list.entities.Task;
import com.shopping_list.exception.BadRequestAlertException;
import com.shopping_list.exception.HeaderUtil;
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
@RequestMapping("/api")
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

/*    @RequestMapping(value = "/{shopId}/task",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Task createTask(@Valid @RequestBody Task task, @PathVariable Long shopId){
        return shoppingRepository.findById(shopId)
                .map(shopping -> {
                    task.setShopping(shopping);
                    return taskRepository.save(task);
                }).orElseThrow(() -> new NotFoundException("Shopping not found!"));
    }*/

    /**
     * {@code POST  /tasks} : Create a new operation.
     *
     * @param task the task to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new task, or with status {@code 400 (Bad Request)} if the task has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tasks")
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task task) throws URISyntaxException {
        log.debug("REST request to save Task : {}", task);
        if (task.getTaskId() != null) {
            throw new BadRequestAlertException("A new operation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        System.out.println(task.getName());
        Task result = taskRepository.save(task);
        return ResponseEntity.created(new URI("/api/tasks/" + result.getTaskId()))
                .headers(HeaderUtil.createEntityCreationAlert("task",  result.getTaskId().toString()))
                .body(result);
    }

    @DeleteMapping("/{taskId}")
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

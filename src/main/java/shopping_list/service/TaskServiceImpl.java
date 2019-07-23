package shopping_list.service;

import com.shopping_list.Repository.TaskRepository;
import com.shopping_list.entities.Shopping;
import com.shopping_list.entities.Task;
import com.shopping_list.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Created by EDOUGA on 21/06/2019.
 */

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;


    @Override
    public List<Task> findAllTask() {
        return taskRepository.findAll();
    }

    @Override
    public Task findTaskId(Long taskId) {
        Optional<Task> task = taskRepository.findById(taskId);
        if (!task.isPresent()) {
            throw new RuntimeException("Task Not Found!");
        }
        return task.get();

    }

    @Override
    public Task createTask(Task task) {

        return   taskRepository.save(task);
    }

    @Override
    public Task updateTask(Task task) {

        Task currentTask = findTaskId(task.getTaskId());
       if (currentTask != null){
           taskRepository.save(currentTask);
       }
       return task;
    }

    @Override
    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }
}


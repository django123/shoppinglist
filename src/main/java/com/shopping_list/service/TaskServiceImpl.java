package com.shopping_list.service;

import com.shopping_list.Repository.TaskRepository;
import com.shopping_list.entities.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by EDOUGA on 21/06/2019.
 */

@Service
public class TaskServiceImpl implements TaskService{

    @Autowired
    private TaskRepository taskRepository;


    @Override
    public List<Task> findAllTask() {
        return taskRepository.findAll();
    }

    @Override
    public Task findTaskId(Long taskId) {
        return taskRepository.findById(taskId).get();
    }

    @Override
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(Long taskId) {
       taskRepository.deleteById(taskId);
    }
}


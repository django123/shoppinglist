package com.shopping_list.service;

import com.shopping_list.entities.Task;

import java.util.List;

/**
 * Created by EDOUGA on 21/06/2019.
 */
public interface TaskService {

    List<Task> findAllTask();
    Task findTaskId(Long taskId);
    Task createTask(Task task);
    Task updateTask(Task task);
    void deleteTask(Long taskId);
}

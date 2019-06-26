package com.shopping_list.service;

import com.shopping_list.entities.Task;

import java.util.List;

/**
 * Created by EDOUGA on 21/06/2019.
 */
public interface TaskService {


    /**
     * gets all Tasks from Database
     * @return  a List containing Tasks
     */
    List<Task> findAllTask();

    /**
     * finds a task  in DB by its ID
     * @param taskId   Database ID of task
     * @return          Book with ID = taskId
     */
    Task findTaskId(Long taskId);


    Task createTask(Task task);

    /**
     * Updates a task  with
     * @param taskId               ID of task
     * @param task           task  details from EDIT FORM
     */
    Task updateTask(Task task,Long taskId);



    /**
     * delete a task from DB
     * @param taskId    ID of task
     */
    void deleteTask(Long taskId);
}

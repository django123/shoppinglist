package com.shopping_list.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Task implements Serializable{
    @Id
    @GeneratedValue
    private Long task_id;
    private String name;
    private String description;
    @ManyToOne
    @JoinColumn
    private Shopping shopping;

    public Task() {
    }

    public Task(String name, String description, Shopping shopping) {
        this.name = name;
        this.description = description;
        this.shopping = shopping;
    }

    public Long getTask_id() {
        return task_id;
    }

    public void setTask_id(Long task_id) {
        this.task_id = task_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Shopping getShopping() {
        return shopping;
    }

    public void setShopping(Shopping shopping) {
        this.shopping = shopping;
    }
}

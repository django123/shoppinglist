package com.shopping_list.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "taskId")
public class Task implements Serializable{
    @Id
    @GeneratedValue
    private Long taskId;
    @Column(name = "name")
    @NotEmpty(message = "*Please enter name of task")
    private String name;
    private String description;
    private Boolean status;
    @JsonBackReference
    @ManyToOne(fetch=FetchType.LAZY, optional = false)
    @JoinColumn
    private Shopping shopping;

    public Task() {
    }

    public Task(String name, String description, Boolean status, Shopping shopping) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.shopping = shopping;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}

package com.shopping_list.entities;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Entity
public class Shopping implements Serializable {

    @Id
    @GeneratedValue
    private Long id_shop;
    private String name;
    private String comment;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date date;
    @ManyToOne
    @JoinColumn
    private Utilisateur utilisateur;
    @OneToMany(mappedBy = "shopping", cascade=CascadeType.ALL)
    @OnDelete(action= OnDeleteAction.NO_ACTION)
    private Collection<Task>tasks;
    @ManyToOne
    @JoinColumn
    private Status status;

    public Shopping() {
    }

    public Shopping(String name, String comment, Date date, Utilisateur utilisateur, Collection<Task> tasks, Status status) {
        this.name = name;
        this.comment = comment;
        this.date = date;
        this.utilisateur = utilisateur;
        this.tasks = tasks;
        this.status = status;
    }

    public Long getId_shop() {
        return id_shop;
    }

    public void setId_shop(Long id_shop) {
        this.id_shop = id_shop;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Collection<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Collection<Task> tasks) {
        this.tasks = tasks;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}

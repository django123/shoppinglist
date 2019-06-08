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
    private Long shopId;
    private String name;
    private String comment;
    private Boolean statut;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date date;
    @ManyToOne
    @JoinColumn
    private Utilisateur utilisateur;
    @OneToMany(mappedBy = "shopping", cascade=CascadeType.ALL)
    @OnDelete(action= OnDeleteAction.NO_ACTION)
    private Collection<Task>tasks;

    public Shopping() {
    }

    public Shopping(String name, String comment, Boolean statut, Date date, Utilisateur utilisateur, Collection<Task> tasks) {
        this.name = name;
        this.comment = comment;
        this.statut = statut;
        this.date = date;
        this.utilisateur = utilisateur;
        this.tasks = tasks;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
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

    public Boolean getStatut() {
        return statut;
    }

    public void setStatut(Boolean statut) {
        this.statut = statut;
    }
}

package com.example.demo.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by EDOUGA on 21/05/2019.
 */

@Entity
public class Shopping implements Serializable{

    @Id
    @GeneratedValue
    private Long id_shop;

    private String name;
    private String comment;
    @DateTimeFormat
    @Temporal(TemporalType.DATE)
    private Date date;

    @ManyToOne
    @JoinColumn
    private User user;

    public Shopping() {

    }

    public Shopping(String name, String comment, Date date, User user) {
        this.name = name;
        this.comment = comment;
        this.date = date;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

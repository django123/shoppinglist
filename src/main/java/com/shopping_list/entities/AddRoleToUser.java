package com.shopping_list.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Entity
public class AddRoleToUser implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    private Long user_id;
    private Long role_id;

    public AddRoleToUser(Long user_id, Long role_id) {
        this.user_id = user_id;
        this.role_id = role_id;
    }

    public AddRoleToUser(List<Role> all, Utilisateur utilisateur) {
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getRole_id() {
        return role_id;
    }

    public void setRole_id(Long role_id) {
        this.role_id = role_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

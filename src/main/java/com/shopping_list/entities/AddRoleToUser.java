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
    private Long userId;
    private Long roleId;

    public AddRoleToUser(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    public AddRoleToUser(List<Role> all, Utilisateur utilisateur) {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

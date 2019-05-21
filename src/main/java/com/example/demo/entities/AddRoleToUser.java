package com.example.demo.entities;

import javax.jws.soap.SOAPBinding;
import java.util.Collection;

/**
 * Created by EDOUGA on 21/05/2019.
 */
public class AddRoleToUser {

    private Long user_id;
    private Long role_id;

    private Collection<Role>roles;
    private User user;


    public AddRoleToUser() {
    }

    public AddRoleToUser(Collection<Role> roles, User user) {
        this.roles = roles;
        this.user = user;
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

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

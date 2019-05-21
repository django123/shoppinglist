package com.example.demo.entities;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Collection;

/**
 * Created by EDOUGA on 21/05/2019.
 */

@Entity
@Table(name = "\"User\"")
public class User {


    @Id
    @GeneratedValue
    private Long user_id;
    private String name;

    @Email(message = "*Please enter a valid email")
    private String email;
    private String password;
    private Boolean active;
    private String type;


    @ManyToMany
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role>roles;

    @OneToMany(mappedBy = "user")
    private Collection<Shopping>shoppings;

    public User() {
    }

    public User(String name, String email, String password, Boolean active, String type, Collection<Role> roles, Collection<Shopping> shoppings) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.active = active;
        this.type = type;
        this.roles = roles;
        this.shoppings = shoppings;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public Collection<Shopping> getShoppings() {
        return shoppings;
    }

    public void setShoppings(Collection<Shopping> shoppings) {
        this.shoppings = shoppings;
    }

    public void addRoles(Role role){
        roles.add(role);
    }
}

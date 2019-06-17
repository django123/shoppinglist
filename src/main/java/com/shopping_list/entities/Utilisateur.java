package com.shopping_list.entities;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Collection;

@Entity
public class Utilisateur implements Serializable {

    @Id
    @GeneratedValue
    private Long userId;
    @Column(name = "email")
    @Email(message = "*Please enter a valid email")
    @NotEmpty(message = "*enter your email")
    private String email;
    @Column(name = "password")
    @Length(min = 8, message = "*the password would have less 8 caracters")
    @NotEmpty(message = "*enter your password")
    private String password;
    private Boolean active;
    private String type;
    private String name;
    @ManyToMany
    @JoinTable(name = "utilisateur_role", joinColumns = @JoinColumn(name = "userId"), inverseJoinColumns = @JoinColumn(name = "roleId"))
    private Collection<Role> roles;

    @ManyToMany(mappedBy = "utilisateurs")
    private Collection<Shopping> shoppings;


    public Utilisateur() {
    }

    public Utilisateur(@Email(message = "*Please enter a valid email") @NotEmpty(message = "*enter your email") String email, @Length(min = 8, message = "*the password would have less 8 caracters") @NotEmpty(message = "*enter your password") String password, Boolean active, String type, String name, Collection<Role> roles, Collection<Shopping> shoppings) {
        this.email = email;
        this.password = password;
        this.active = active;
        this.type = type;
        this.name = name;
        this.roles = roles;
        this.shoppings = shoppings;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addRoles(Role role){
        roles.add(role);
    }

    public void removeRelation(Role role) {
        roles.remove(role);
        role.getUtilisateurs().remove(this);
    }

    public void setRoles(Role role){
        roles.add(role);
    }

    public Collection<Shopping> getShoppings() {
        return shoppings;
    }

    public void setShoppings(Collection<Shopping> shoppings) {
        this.shoppings = shoppings;
    }
}

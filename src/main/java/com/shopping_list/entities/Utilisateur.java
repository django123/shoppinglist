package com.shopping_list.entities;

import com.fasterxml.jackson.annotation.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "userId")
public class Utilisateur implements Serializable{

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long userId;
    @Email(message = "*Please enter a valid email")
    @NotEmpty
    private String email;
    @NotEmpty
    private String username;
    @NotEmpty
    @Size(min = 3, message = "Length must be more than 3")
    private String password;
    @NotNull
    private Boolean active;
    @NotEmpty
    private String role = "USER";
    @JsonBackReference
    @ManyToMany
    @JoinTable(name = "utilisateur_role", joinColumns = @JoinColumn(name = "userId"), inverseJoinColumns = @JoinColumn(name = "roleId"))
    private Collection<Role> roles;

    @ManyToMany(mappedBy = "utilisateurs")
    private Collection<Shopping> shoppings;


    public Utilisateur() {
    }

    public Utilisateur(@Email(message = "*Please enter a valid email") @NotEmpty(message = "*enter your email") String email, String username, String password, Boolean active, Collection<Role> roles, Collection<Shopping> shoppings) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.active = active;
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

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }


    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void addRoles(Role role){
        roles.add(role);
    }

    public void removeRelation(Role role) {
        roles.remove(role);
        role.getUtilisateurs().remove(this);
    }

    @JsonIgnore
    @JsonBackReference
    public void setRoles(Role role){
        roles.add(role);
    }

    public Collection<Shopping> getShoppings() {
        return shoppings;
    }

    public void setShoppings(Collection<Shopping> shoppings) {
        this.shoppings = shoppings;
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

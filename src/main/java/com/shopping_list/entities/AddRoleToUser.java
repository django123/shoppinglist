package com.shopping_list.entities;

import java.util.Collection;


public class AddRoleToUser {

    private Long user_id;
    private Long role_id;
    private Collection<Role> roles;
    private Utilisateur utilisateur;

    public AddRoleToUser(Collection<Role> roles, Utilisateur utilisateur) {
        this.roles = roles;
        this.utilisateur = utilisateur;
    }


    public AddRoleToUser() {
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

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }
}

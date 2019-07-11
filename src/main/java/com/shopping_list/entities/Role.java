package com.shopping_list.entities;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.io.Serializable;
import java.util.Collection;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "roleId")
public class Role implements Serializable {

    @Id
    @GeneratedValue
    private Long roleId;

    private String name;

    @ManyToMany(mappedBy = "roles")
    private Collection<Utilisateur> utilisateurs;

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Utilisateur> getUtilisateurs() {
        return utilisateurs;
    }

    public void setUtilisateurs(Collection<Utilisateur> utilisateurs) {
        this.utilisateurs = utilisateurs;
    }
}

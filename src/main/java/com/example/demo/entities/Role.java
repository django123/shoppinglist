package com.example.demo.entities;

import org.omg.CORBA.PRIVATE_MEMBER;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.io.Serializable;
import java.util.Collection;

/**
 * Created by EDOUGA on 21/05/2019.
 */
@Entity
public class Role  implements Serializable {

    @Id
    @GeneratedValue
    private Long role_id;
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Collection<User>users;

    public Role(String name) {
        this.name = name;
    }

    public Role() {
    }

    public Long getRole_id() {
        return role_id;
    }

    public void setRole_id(Long role_id) {
        this.role_id = role_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }
}

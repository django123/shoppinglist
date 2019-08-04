package com.shopping_list.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data @AllArgsConstructor
public class AppRole {

    @Id
    @GeneratedValue
    private Long roleId;

    private String role;

    public AppRole() {
    }

    public AppRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

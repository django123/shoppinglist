package com.shopping_list.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class AppRole {

    @Id
    @GeneratedValue
    private Long id;

    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

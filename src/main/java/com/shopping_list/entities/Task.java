package com.shopping_list.entities;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Task implements Serializable{

    @Id
    @GeneratedValue
    private Long taskId;
    @Column(name = "name")
    @NotNull
    private String name;
    private String description;
    private Boolean status;
    @ManyToOne(fetch=FetchType.LAZY, optional = false)
    @JsonIgnoreProperties("tasks")
    private Shopping shopping;
}

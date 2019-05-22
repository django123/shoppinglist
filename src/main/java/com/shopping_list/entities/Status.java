package com.shopping_list.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Entity
public class Status implements Serializable {
    @Id
    @GeneratedValue
    private Long status_id;
    private boolean state;
    @OneToMany(mappedBy = "status")
    private Collection<Shopping>shoppings;

    public Status(boolean state, Collection<Shopping> shoppings) {
        this.state = state;
        this.shoppings = shoppings;
    }

    public Status() {
    }

    public Long getStatus_id() {
        return status_id;
    }

    public void setStatus_id(Long status_id) {
        this.status_id = status_id;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public Collection<Shopping> getShoppings() {
        return shoppings;
    }

    public void setShoppings(Collection<Shopping> shoppings) {
        this.shoppings = shoppings;
    }
}

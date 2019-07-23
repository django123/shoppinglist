package shopping_list.entities;

import com.fasterxml.jackson.annotation.*;
import com.shopping_list.entities.Shopping;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
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

    public Task() {
    }

    public Task(String name, String description, Boolean status, Shopping shopping) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.shopping = shopping;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Shopping getShopping() {
        return shopping;
    }

    public void setShopping(Shopping shopping) {
        this.shopping = shopping;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}

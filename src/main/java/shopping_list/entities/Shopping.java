package shopping_list.entities;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.shopping_list.entities.Task;
import com.shopping_list.entities.Utilisateur;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import org.hibernate.annotations.Cache;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "shopId")
public class Shopping implements Serializable {

    @Id
    @GeneratedValue
    private Long shopId;
    @Column(name = "name")
    @NotNull
    private String name;
    private String comment;
    private Boolean statut;
    private Boolean archived;
    private Boolean shared;
    private String saverName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date date;
    @OneToMany(mappedBy = "shopping", cascade=CascadeType.ALL)
    @OnDelete(action= OnDeleteAction.NO_ACTION)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Collection<com.shopping_list.entities.Task>tasks;

    @ManyToMany
    @JoinTable(name = "shopping_utilisateur", joinColumns = @JoinColumn(name = "shopId"), inverseJoinColumns = @JoinColumn(name = "userId"))
    private Collection<Utilisateur> utilisateurs;

    public Shopping() {
    }

    public Shopping(String name, String comment, Boolean statut, Boolean archived, Boolean shared, String saverName, Date date, Collection<com.shopping_list.entities.Task> tasks, Collection<Utilisateur> utilisateurs) {
        this.name = name;
        this.comment = comment;
        this.statut = statut;
        this.archived = archived;
        this.shared = shared;
        this.saverName = saverName;
        this.date = date;
        this.tasks = tasks;
        this.utilisateurs = utilisateurs;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public Collection<com.shopping_list.entities.Task> getTasks() {
        return tasks;
    }

    public void setTasks(Collection<Task> tasks) {
        this.tasks = tasks;
    }

    public Boolean getStatut() {
        return statut;
    }

    public void setStatut(Boolean statut) {
        this.statut = statut;
    }

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public Collection<Utilisateur> getUtilisateurs() {
        return utilisateurs;
    }

    public void setUtilisateurs(Collection<Utilisateur> utilisateurs) {
        this.utilisateurs = utilisateurs;
    }

    public void add(Utilisateur utilisateur){
        utilisateurs.add(utilisateur);

    }

    public void remove(Utilisateur utilisateur){
        utilisateurs.remove(utilisateur);
        utilisateur.getShoppings().remove(this);
    }

    public String getSaverName() {
        return saverName;
    }

    public void setSaverName(String saverName) {
        this.saverName = saverName;
    }

    public Boolean getShared() {
        return shared;
    }

    public void setShared(Boolean shared) {
        this.shared = shared;
    }

}

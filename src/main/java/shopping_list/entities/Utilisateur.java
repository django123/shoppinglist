package shopping_list.entities;

import com.fasterxml.jackson.annotation.*;
import com.shopping_list.entities.Role;
import com.shopping_list.entities.Shopping;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "userId")
public class Utilisateur implements Serializable,UserDetails{

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long userId;
    @Email(message = "*Please enter a valid email")
    @NotEmpty(message = "*enter your email")
    private String email;
    private String username;
    private String password;
    private Boolean active;
    @NotEmpty
    private String role = "USER";
    @JsonBackReference
    @ManyToMany
    @JoinTable(name = "utilisateur_role", joinColumns = @JoinColumn(name = "userId"), inverseJoinColumns = @JoinColumn(name = "roleId"))
    private Collection<com.shopping_list.entities.Role> roles;

    @ManyToMany(mappedBy = "utilisateurs")
    private Collection<com.shopping_list.entities.Shopping> shoppings;


    public Utilisateur() {
    }

    public Utilisateur(@Email(message = "*Please enter a valid email") @NotEmpty(message = "*enter your email") String email, String username, String password, Boolean active, Collection<com.shopping_list.entities.Role> roles, Collection<com.shopping_list.entities.Shopping> shoppings) {
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities= new HashSet<>();
        for(com.shopping_list.entities.Role role: roles) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getName());
            authorities.add(grantedAuthority);
        }

        return authorities;
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

    public Collection<com.shopping_list.entities.Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<com.shopping_list.entities.Role> roles) {
        this.roles = roles;
    }


    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void addRoles(com.shopping_list.entities.Role role){
        roles.add(role);
    }

    public void removeRelation(com.shopping_list.entities.Role role) {
        roles.remove(role);
        role.getUtilisateurs().remove(this);
    }

    @JsonIgnore
    @JsonBackReference
    public void setRoles(Role role){
        roles.add(role);
    }

    public Collection<com.shopping_list.entities.Shopping> getShoppings() {
        return shoppings;
    }

    public void setShoppings(Collection<Shopping> shoppings) {
        this.shoppings = shoppings;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

package com.shopping_list.Repository;

import com.shopping_list.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Utilisateur,Long> {
    Utilisateur findByEmail(String email);
    List<Utilisateur> findByName(String name);
}

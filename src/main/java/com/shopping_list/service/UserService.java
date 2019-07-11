package com.shopping_list.service;

import com.shopping_list.entities.Utilisateur;

import java.util.List;

/**
 * Created by EDOUGA on 19/06/2019.
 */

public interface UserService {

    public Utilisateur createUser(Utilisateur user);

    Utilisateur addUser(Utilisateur user);
    public Utilisateur findByUsername(String name);
    public Utilisateur findUserByEmail(String email);
    List<Utilisateur>findAllUtilisateur();
}

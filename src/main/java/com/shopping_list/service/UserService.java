package com.shopping_list.service;

import com.shopping_list.entities.Utilisateur;

import java.util.List;

/**
 * Created by EDOUGA on 19/06/2019.
 */

public interface UserService {

    public void createUser(Utilisateur user);


    public List<Utilisateur>findByName(String name);
    public Utilisateur findUserByEmail(String email);
}

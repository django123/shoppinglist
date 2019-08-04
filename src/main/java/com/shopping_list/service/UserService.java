package com.shopping_list.service;

import com.shopping_list.entities.AppUser;

public interface UserService {

    AppUser findByUsername(String username);
    void createUser(AppUser user);
}

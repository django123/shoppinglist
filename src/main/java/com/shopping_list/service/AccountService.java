package com.shopping_list.service;


import com.shopping_list.entities.AppRole;
import com.shopping_list.entities.AppUser;
import javassist.tools.rmi.ObjectNotFoundException;

import java.util.List;

public interface AccountService {

    AppUser saveUser(AppUser user);
    AppRole saveRole(AppRole role);
    AppUser findUserByUsername(String username);
    void addRoleToUser(String username, String role);
    AppUser findById(Long id) throws ObjectNotFoundException;
    List<AppUser> findAllUser();


}

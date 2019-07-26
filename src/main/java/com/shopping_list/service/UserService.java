package com.shopping_list.service;

import com.shopping_list.entities.User;
import javassist.tools.rmi.ObjectNotFoundException;

import java.util.List;

public interface UserService {
    User findByUsernameOrEmail(String usermaneOrEmail);
    User findById(Long id) throws ObjectNotFoundException;
    List<User> findAllUser();
    User createUser(User user);
    public User findByUsername(String name);

}

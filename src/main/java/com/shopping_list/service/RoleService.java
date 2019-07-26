package com.shopping_list.service;


import com.shopping_list.entities.Role;
import javassist.tools.rmi.ObjectNotFoundException;

import java.util.List;

public interface RoleService {

    List<Role> findAll()throws ObjectNotFoundException;
}

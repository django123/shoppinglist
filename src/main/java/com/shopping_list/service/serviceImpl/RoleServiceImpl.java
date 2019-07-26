package com.shopping_list.service.serviceImpl;


import com.shopping_list.Repository.RoleRepository;
import com.shopping_list.entities.Role;
import com.shopping_list.service.RoleService;
import javassist.tools.rmi.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service("roleService")
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Role> findAll() throws ObjectNotFoundException {

        List<Role> roles = roleRepository.findAll();
        if(roles.isEmpty()){
            throw new ObjectNotFoundException("Role list is empty");
        }
        return roles;

    }
}

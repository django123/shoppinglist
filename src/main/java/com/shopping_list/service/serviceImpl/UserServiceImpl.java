package com.shopping_list.service.serviceImpl;

import com.shopping_list.Repository.RoleRepository;
import com.shopping_list.Repository.UserRepository;
import com.shopping_list.entities.Role;
import com.shopping_list.entities.User;
import com.shopping_list.service.UserService;
import javassist.tools.rmi.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    @Transactional(readOnly = true)
    public User findByUsernameOrEmail(String usernameOrEmail) {
        try {
            User user = userRepository.findByUsernameOrEmail(usernameOrEmail);
            return user;
        } catch (Exception e) {
            throw e;
        }

    }

    @Override
    @Transactional(readOnly = true)
    public User findById(Long id) throws ObjectNotFoundException {
        User user = userRepository.findById(id).get();
        if (user == null) {
            throw new ObjectNotFoundException("User not found");
        }
        return user;
    }

    @Override
    public List<User> findAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(User user) {
        BCryptPasswordEncoder encoder = new  BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByRoleName("USER");
        if (userRole != null) {
            user.setRole(userRole);
        }else{
            Role role=new Role("USER");
            roleRepository.save(role);
            user.setRole(role);
        }
        return userRepository.save(user);
    }
    @Override
    public User findByUsername(String name) {
        return  userRepository.findByUsername(name);
    }

}

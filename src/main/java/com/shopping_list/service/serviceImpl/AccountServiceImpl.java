package com.shopping_list.service.serviceImpl;


import com.shopping_list.Repository.AppRoleRepository;
import com.shopping_list.Repository.AppUserRepository;
import com.shopping_list.entities.AppRole;
import com.shopping_list.entities.AppUser;
import com.shopping_list.service.AccountService;
import javassist.tools.rmi.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private AppRoleRepository roleRepository;



    @Override
    public AppUser saveUser(AppUser user) {
        BCryptPasswordEncoder encoder = new  BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public AppRole saveRole(AppRole r) {
        return roleRepository.save(r);
    }

    @Override
    public AppUser findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void addRoleToUser(String username, String role) {
        AppUser user = userRepository.findByUsername(username);
        AppRole role1 = roleRepository.findByRole(role);
        if (role1!=null){
            user.getRoles().add(role1);
        }else{
            AppRole role2 = new AppRole();
            role2.setRole(role);
            roleRepository.save(role2);

            user.getRoles().add(roleRepository.findByRole(role));
        }

    }


    @Override
    public AppUser findById(Long id) throws ObjectNotFoundException {
        return userRepository.findById(id).get();
    }

    @Override
    public List<AppUser> findAllUser() {
        return userRepository.findAll();
    }


}

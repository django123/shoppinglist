package shopping_list.service;

import com.shopping_list.Repository.RoleRepository;
import com.shopping_list.Repository.UserRepository;
import com.shopping_list.entities.Role;
import com.shopping_list.entities.Utilisateur;
import com.shopping_list.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created by EDOUGA on 19/06/2019.
 */
@Service
public class UserServiceImpl implements com.shopping_list.service.UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public Utilisateur createUser(Utilisateur user) {
        BCryptPasswordEncoder encoder = new  BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        user.setActive(true);
        Role userRole = roleRepository.findByName("USER");
        if (userRole != null) {
            user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        }else{
            Role role=new Role("USER");
            roleRepository.save(role);
            user.setRoles(new HashSet<Role>(Arrays.asList(role)));
        }
        return userRepository.save(user);

    }


    @Override
    public Utilisateur findByUsername(String name) {
        return  userRepository.findByUsername(name);
    }

    @Override
    public Utilisateur findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Utilisateur findOne(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<Utilisateur> findAllUtilisateur() {
        return userRepository.findAll();
    }


}

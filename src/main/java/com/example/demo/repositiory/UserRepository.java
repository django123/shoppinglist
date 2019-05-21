package com.example.demo.repositiory;

import com.example.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by EDOUGA on 21/05/2019.
 */
@Repository
public interface UserRepository extends JpaRepository<User,Long>{

    User findByEmail(String email);
}

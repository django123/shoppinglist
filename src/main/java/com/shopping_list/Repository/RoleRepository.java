package com.shopping_list.Repository;

import com.shopping_list.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;   
@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    Role findByName(String name);

}

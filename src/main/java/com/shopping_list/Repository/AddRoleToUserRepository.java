package com.shopping_list.Repository;

import com.shopping_list.entities.AddRoleToUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddRoleToUserRepository extends JpaRepository<AddRoleToUser, Long> {

//   AddRoleToUser findByUser_id(Long user_id);
}

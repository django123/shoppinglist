package com.shopping_list.Repository;

import com.shopping_list.entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;



@Repository
public interface StatusRepository extends JpaRepository<Status,Long> {

}
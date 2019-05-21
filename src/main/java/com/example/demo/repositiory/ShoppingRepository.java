package com.example.demo.repositiory;

import com.example.demo.entities.Shopping;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by EDOUGA on 21/05/2019.
 */

@Repository
public interface ShoppingRepository extends JpaRepository<Shopping, Long>{

   @Query("select s from Shopping as s join s.user su where su.user_id =: id order by s.id_shop desc")
   List<Shopping>findAllByUserOrderById_shopDesc(@Param("id")Long id_shop);
}

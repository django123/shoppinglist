package com.shopping_list.Repository;

import com.shopping_list.entities.Shopping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ShoppingRepository extends JpaRepository<Shopping,Long> {

    @Query("select s from Shopping as s join s.utilisateur su where su.user_id =: id order by s.id_shop desc ")
    List<Shopping>findAllByUtilisateurOrderById_shopDesc(@Param("id") Long user_id);
    @Query("select s from Shopping as s join s.status se where se.status_id =: id order by s.id_shop desc ")
    List<Shopping> findAllByStatusOrderById_shopDesc(@Param("id") Long status_id);

}

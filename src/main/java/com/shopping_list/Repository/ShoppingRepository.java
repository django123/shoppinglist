package com.shopping_list.Repository;

import com.shopping_list.entities.Shopping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ShoppingRepository extends JpaRepository<Shopping,Long> {

    @Query("select s from Shopping as s join s.utilisateur su where su.userId =: id order by s.shopId desc ")
    List<Shopping>findAllByUtilisateurOrderByShopIdDesc(@Param("id") Long userId);
    List<Shopping> findByArchived(Boolean archived);

}

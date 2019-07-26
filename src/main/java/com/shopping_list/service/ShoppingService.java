package com.shopping_list.service;


import com.shopping_list.entities.Shopping;

import java.util.List;

/**
 * Created by EDOUGA on 21/06/2019.
 */
public interface ShoppingService {


    /**
     * gets all Shoppings from Database
     * @return  a List containing Shoppings
     */
    List<Shopping> findAllShopping();



    /**
     * finds a shopping  in DB by its ID
     * @param shopId    Database ID of shopping
     * @return          Shopping with ID = shopId
     */
    Shopping findShoppingId(Long shopId);






    /**
     * Create  a shopping  with
     * @param shopping           shopping  details from EDIT FORM
     */
    Shopping createShopping(Shopping shopping);

    /**
     * Update a shopping  with
     * @param shopping           shopping  details from EDIT FORM
     */
    Shopping updateShopping(Shopping shopping);


    /**
     * delete a shopping from DB
     * @param shopId    ID of Shopping
     */
    void deleteShopping(Long shopId);

}

package com.shopping_list.service;

import com.shopping_list.entities.Shopping;

import java.util.List;

/**
 * Created by EDOUGA on 21/06/2019.
 */
public interface ShoppingService {


   List<Shopping>findAllShopping();
    Shopping findShoppingId(Long shopId);
    Shopping createShopping(Shopping shopping);
    Shopping updateShopping(Shopping shopping);
    void deleteShopping(Long shopId);
}

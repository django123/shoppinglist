package com.shopping_list.service;

import com.shopping_list.Repository.ShoppingRepository;
import com.shopping_list.entities.Shopping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Created by EDOUGA on 21/06/2019.
 */
@Service
public class ShoppingServiceImpl implements ShoppingService{


    @Autowired
    private ShoppingRepository shoppingRepository;


    @Override
    public List<Shopping> findAllShopping() {
        return shoppingRepository.findAll();
    }



    /**
     * finds a Shopping in DB by its ID
     * @param shopId    Database ID of Shopping
     * @return          Book with ID = shopId
     */

    @Override
    public Shopping findShoppingId(Long shopId) {
        Optional<Shopping> ShoppingOptional = shoppingRepository.findById(shopId);

        if (!ShoppingOptional.isPresent()) {
            throw new RuntimeException("Shopping Not Found!");
        }
        return ShoppingOptional.get();
    }

    @Override
    public Long createShopping(Shopping shopping) {
        shoppingRepository.save(shopping);
        return shopping.getShopId() ;
    }

    @Override
    public void updateShopping(Shopping shopping, Long shopId) {

        Shopping currentShopping = findShoppingId(shopId);
        currentShopping.setName(shopping.getName());
        currentShopping.setComment(shopping.getComment());
        currentShopping.setDate(shopping.getDate());
        currentShopping.setShared(shopping.getShared());
        currentShopping.setStatut(shopping.getStatut());
        currentShopping.setSaverName(shopping.getSaverName());

        shoppingRepository.save(currentShopping);

    }


    /**
     * delete a Shopping from DB
     * @param shopId     ID of Shopping
     */
    @Override
    public void deleteShopping(Long shopId) {
       shoppingRepository.deleteById(shopId);
    }
}

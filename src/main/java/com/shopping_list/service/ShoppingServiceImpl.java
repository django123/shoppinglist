package com.shopping_list.service;

import com.shopping_list.Repository.ShoppingRepository;
import com.shopping_list.entities.Shopping;
import com.shopping_list.service.ShoppingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Created by EDOUGA on 21/06/2019.
 */
@Service
public class ShoppingServiceImpl implements ShoppingService {


    @Autowired
    private ShoppingRepository shoppingRepository;


    @Override
    public List<Shopping> findAllShopping() {
        return shoppingRepository.findAll();
    }




    @Override
    public Shopping findShoppingId(Long shopId) {
        Optional<Shopping> ShoppingOptional = shoppingRepository.findById(shopId);

        if (!ShoppingOptional.isPresent()) {
            throw new RuntimeException("Shopping Not Found!");
        }
        return ShoppingOptional.get();
    }

    @Override
    public Shopping createShopping(Shopping shopping) {
        return shoppingRepository.save(shopping);
    }

    @Override
    public Shopping updateShopping(Shopping shopping) {
        Shopping shopping1 = findShoppingId(shopping.getShopId());
        if (shopping1 != null){
            shoppingRepository.save(shopping1);
        }
        return shopping;
    }


    @Override
    public void deleteShopping(Long shopId) {
       shoppingRepository.deleteById(shopId);
    }
}

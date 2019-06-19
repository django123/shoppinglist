package com.shopping_list.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by EDOUGA on 17/06/2019.
 */
@Entity
public class Share implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private Long userId;

    private Long shopId;

    public Share() {
    }

    public Share(Long shopId, Long userId) {
        this.shopId = shopId;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }
}

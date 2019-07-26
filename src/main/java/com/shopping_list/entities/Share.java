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
    private Long shareId;

    private Long id;

    private Long shopId;

    public Share() {
    }

    public Share(Long shopId, Long id) {
        this.shopId = shopId;
        this.id = id;
    }

    public Long getShareId() {
        return shareId;
    }

    public void setShareId(Long shareId) {
        this.shareId = shareId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }
}

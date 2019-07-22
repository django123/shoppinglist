package com.shopping_list.vo;

import lombok.Data;

@Data
public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private String username;


    public JwtResponse(String token, String username) {
        this.username = username;
        this.token = token;

    }
}


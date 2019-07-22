package com.shopping_list.RestController;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class TestRescontroller {


    @GetMapping("/public")
    public String publicService() {
        return "This message is public";
    }

    @GetMapping("/secret")
    public String secretService() {
        return "A secret message";
    }
}

package com.shopping_list.controller;

import com.shopping_list.Repository.ShoppingRepository;
import com.shopping_list.entities.AppUser;
import com.shopping_list.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class HomeController {

    @Autowired
    private AccountService userService;

    @Autowired
    private ShoppingRepository shoppingRepository;
    @GetMapping("/")
    public String home(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AppUser user = userService.findUserByUsername(auth.getName());

        model.addAttribute("userName", "Welcome " + user.getUsername());


        return "redirect:/shopping/all";

    }

    @GetMapping("/login")
    public String showLoginForm() {

        return "login";
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request){

        HttpSession session = request.getSession();
        session.invalidate();
        return "redirect:/login";
    }

}

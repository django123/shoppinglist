package com.shopping_list.RestController;

import com.shopping_list.Repository.*;
import com.shopping_list.entities.AppUser;
import com.shopping_list.entities.Share;
import com.shopping_list.entities.Shopping;
import com.shopping_list.entities.Task;
import com.shopping_list.service.AccountService;
import com.shopping_list.service.ShoppingService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.*;



import org.slf4j.Logger;


@RestController
public class ShoppingRestController {
    private final Logger log = LoggerFactory.getLogger(ShoppingRestController.class);

    @Autowired
    private ShoppingRepository shoppingRepository;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private ShoppingService shoppingService;

    @Autowired
    private AccountService accountService;
    @GetMapping("/shoppings")
    public ResponseEntity<Collection<Shopping>> listShopping()
            throws URISyntaxException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AppUser user = accountService.findUserByUsername(auth.getName());
        System.out.println(user);
        List<Shopping>shoppings1 = shoppingRepository.findByArchived(false);
        List<Shopping>shoppings2 = shoppingRepository.findByUsers_Id(user.getId());
        List<Shopping> shoppings= new ArrayList<>();
        for (Shopping shopping : shoppings1){
            for (int i=0; i<shoppings2.size(); i++){
                if (shopping.getShopId().equals(shoppings2.get(i).getShopId())){
                    shoppings.add(shoppings2.get(i));

                }
            }

        }
        for(Shopping shopping: shoppings){
            List<Task>tasks = new ArrayList<>();
            tasks.addAll(shopping.getTasks());
            List<Task>tasks1=new ArrayList<>();
            for (Task task:tasks){
                if (task.getStatus()!=null && task.getStatus()){
                    tasks1.add(task);
                }
            }
            if ((tasks.size() != 0) && tasks.size()==tasks1.size()){
                shopping.setStatut(true);
            }
            System.out.println(shoppings);
        }
        List<Integer> numbers= new ArrayList<>();
        for (Shopping shopping : shoppings){
            Collection<Task> tasks= shopping.getTasks();
            Collection<Task> tasks1=new ArrayList<>();

            for (Task task:tasks){
                if (task.getStatus() != null  && task.getStatus() == true){
                    tasks1.add(task);
                }
            }
            int nombre = tasks1.size();
            numbers.add(nombre);
        }

        log.debug("REST request to get a page of shopping");
        //Collection<Shopping> collection = shoppingService.findAllShopping();
        return new ResponseEntity<>(shoppings, null, HttpStatus.OK);
    }

    @PostMapping("/shoppings/create")
    public ResponseEntity<Object> createShopping(@RequestBody Shopping shopping) throws URISyntaxException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AppUser user = accountService.findUserByUsername(auth.getName());
        shopping.setUsers(new HashSet<AppUser>(Arrays.asList(user)));
        shopping.setSaverName(user.getUsername());
        shopping.setArchived(false);
        shopping.setStatut(false);
        shopping.setShared(false);
        Shopping shopping1=shoppingService.createShopping(shopping);

        return ResponseEntity.created(new URI("/shoppings" + shopping1.getShopId())).body(shopping1);
    }
    @GetMapping("/shoppings/{shopId}")
    public ResponseEntity<Shopping> findShopping(@PathVariable Long shopId, HttpSession session){
        log.debug("REST request to get shopping: {}", shopId);
        Shopping shopping = shoppingRepository.findById(shopId).get();
        session.setAttribute("shopId",shopping.getShopId());
        return new ResponseEntity<>(shopping, null,HttpStatus.OK);
    }

    @PutMapping("/shoppings/{shopId}")
    public Shopping updateShopping(@Valid @RequestBody Shopping shopping1, @PathVariable("shopId") Long shopId, String name,
                                   String comment)throws URISyntaxException {

        Shopping shopping = shoppingRepository.findById(shopId).get();
        if (shopping1.getName() != null)
            shopping.setName(shopping1.getName());
        if (shopping1.getComment() != null)
            shopping.setComment(shopping1.getComment());
        if (shopping1.getDate() != null)
            shopping.setDate(shopping1.getDate());
        shopping1.setName(name);
        shopping1.setComment(comment);

        shoppingService.createShopping(shopping);
        return shopping1;
    }

    @DeleteMapping("/shoppings/{shopId}")
    public void deleteShopping(@PathVariable("shopId") Long shopId){
        shoppingService.deleteShopping(shopId);
    }
}

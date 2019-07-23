package com.shopping_list.RestController;


import com.shopping_list.Repository.ShareRepository;
import com.shopping_list.Repository.ShoppingRepository;
import com.shopping_list.Repository.TaskRepository;
import com.shopping_list.Repository.UserRepository;
import com.shopping_list.entities.Share;
import com.shopping_list.entities.Shopping;
import com.shopping_list.entities.Task;
import com.shopping_list.entities.Utilisateur;

import com.shopping_list.service.ShoppingService;
import com.shopping_list.service.UserService;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Stream;

import org.slf4j.Logger;


@CrossOrigin
@RestController
@RequestMapping("/api/shopping")
public class ShoppingRestController {

    private final Logger log = LoggerFactory.getLogger(ShoppingRestController.class);

    @Autowired
    private ShoppingRepository shoppingRepository;

    @Autowired
    private ShoppingService shoppingService;

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ShareRepository shareRepository;


/*     @GetMapping
    public List<Shopping> findAllShopping(){
        List<Shopping>shoppings = shoppingService.findAllShopping();
        return shoppings;
    }*/
     @GetMapping
     public List<Object> findAllShopping(){
        List<Object> objects= new ArrayList<Object>();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        Utilisateur user = userService.findUserByEmail(userDetails.getUsername());
        List<Shopping>shoppings1 = shoppingRepository.findByArchived(false);
        System.out.println(auth);
        List<Shopping>shoppings2 = shoppingRepository.findByUtilisateurs_UserId(user.getUserId());
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
        Stream.of(numbers,userService.findAllUtilisateur(),shoppings).forEach(objects::addAll);

        return objects;
    }

    @RequestMapping(value = "/{shopId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Shopping> findShopping(@PathVariable Long shopId, HttpSession session){
        log.debug("REST request to get shopping: {}", shopId);
        Shopping shopping = shoppingService.findShoppingId(shopId);
        session.setAttribute("shopId",shopping.getShopId());
        return new ResponseEntity<>(shopping, null,HttpStatus.OK);
    }

   @PostMapping
   public Shopping createShopping(@RequestBody Shopping shopping){
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       Utilisateur user = userService.findUserByEmail(auth.getName());
       shopping.setUtilisateurs(new HashSet<Utilisateur>(Arrays.asList(user)));
       shopping.setArchived(false);
       shopping.setStatut(false);
       shopping.setShared(false);
       Shopping shopping1 =shoppingService.createShopping(shopping);

       return shopping1;

    }

    @RequestMapping(value = "/{shopId}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Shopping updateShopping(@Valid @RequestBody Shopping shopping1, @PathVariable("shopId") Long shopId, String name,
                                                  String comment, String archived, String statut,
                                                   String saverName, String shared)throws URISyntaxException {

        Shopping shopping = shoppingService.findShoppingId(shopId);
        if (shopping1.getName() != null)
            shopping.setName(shopping1.getName());
        if (shopping1.getComment() != null)
            shopping.setComment(shopping1.getComment());
        if (shopping1.getDate() != null)
            shopping.setDate(shopping1.getDate());
        if (shopping1.getStatut() != null)
            shopping.setStatut(shopping1.getStatut());
        shopping1.setName(name);
        shopping1.setComment(comment);
        shopping1.setArchived(Boolean.parseBoolean(archived));
        shopping1.setStatut(Boolean.parseBoolean(statut));
        shopping1.setSaverName(saverName);

        shoppingService.updateShopping(shopping);
        return shopping1;
    }

    @RequestMapping(value = "/{shopId}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteShopping(@PathVariable("shopId") Long shopId){
       shoppingService.deleteShopping(shopId);
    }

    @PostMapping("/share/user")
    public List<Shopping>shareShopping(Share share, String userId, String shopId){
        Utilisateur user = userRepository.getOne(Long.parseLong(userId));
        Shopping shopping = shoppingRepository.getOne(Long.parseLong(shopId));
        shopping.add(user);
        shopping.setShared(true);
        share.setUserId(user.getUserId());
        share.setShopId(shopping.getShopId());
        shoppingRepository.save(shopping);
        shareRepository.save(share);
        return shoppingService.findAllShopping();
    }

}

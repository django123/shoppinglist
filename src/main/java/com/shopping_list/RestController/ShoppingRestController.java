package com.shopping_list.RestController;

import com.shopping_list.Repository.ShareRepository;
import com.shopping_list.Repository.ShoppingRepository;
import com.shopping_list.Repository.TaskRepository;
import com.shopping_list.Repository.UserRepository;
import com.shopping_list.entities.Share;
import com.shopping_list.entities.Shopping;
import com.shopping_list.entities.User;
import com.shopping_list.service.ShoppingService;
import com.shopping_list.service.UserService;
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

import java.net.URISyntaxException;

import java.util.*;


import org.slf4j.Logger;


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


    @RequestMapping(value = "/all", method = { RequestMethod.GET })
    public ResponseEntity<List<Shopping>> getAllShopping(Authentication authentication) throws URISyntaxException {
        List<Shopping> shoppings = shoppingService.findAllShopping();
        System.out.println(shoppings);
        return new ResponseEntity(shoppings, null, HttpStatus.OK);//essai un peut
       /* UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        System.out.println(userDetails);
        List<Object> objects= new ArrayList<Object>();
        User user = userService.findByUsernameOrEmail(userDetails.getUsername());
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
        //Stream.of(numbers,userService.findAllUtilisateur(),shoppings).forEach(objects::addAll);

        return  new ResponseEntity(shoppings, null, HttpStatus.OK);*/
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

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
   public Shopping createShopping(@RequestBody Shopping shopping){
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


    @RequestMapping(value = "/share/user",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Shopping>shareShopping(Share share, String userId, String shopId){
        User user = userRepository.getOne(Long.parseLong(userId));
        Shopping shopping = shoppingRepository.getOne(Long.parseLong(shopId));
        shopping.add(user);
        shopping.setShared(true);
        share.setId(user.getId());
        share.setShopId(shopping.getShopId());
        shoppingRepository.save(shopping);
        shareRepository.save(share);
        return shoppingService.findAllShopping();
    }
    @RequestMapping(value = "/archive",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Shopping>findAllArchive(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsernameOrEmail(auth.getName());
        List<Shopping>shoppings = shoppingRepository.findByArchived(true);
        List<Shopping>shoppings2 = shoppingRepository.findByUsers_Id(user.getId());
        List<Shopping>shoppings1 = new ArrayList<>();
        for(Shopping shopping : shoppings){
            for (int i=0; i<shoppings2.size(); i++ ){
                if (shopping.getShopId().equals(shoppings2.get(i).getShopId())){
                    shoppings1.add(shopping);
                }
            }
        }
        return shoppings1;
    }

}

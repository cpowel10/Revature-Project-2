package com.revature.project1.controller;

import com.revature.project1.annotations.Authorized;
import com.revature.project1.model.Role;
import com.revature.project1.model.User;
import com.revature.project1.services.AuthorizationService;
import com.revature.project1.services.ItemService;
import com.revature.project1.services.UserService;
import io.micrometer.core.instrument.MeterRegistry;
import io.prometheus.client.Counter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired()
    User user;

    @Autowired
    UserService userService;

    @Autowired
    ItemService itemService;


    @Autowired
    private AuthorizationService authorizationService;

    boolean result;

    @Autowired
    private static MeterRegistry meterRegistry;

    static final Counter counter = Counter.build()
            .name("total_request")
            .help("Total number of requests")
            .register();

    @PostMapping("/register") //localhost:8088/register
    public ResponseEntity<String> register(@RequestBody User user){
        if(user == null){
            LOGGER.error("User given is null");
            return new ResponseEntity<>("User is null", HttpStatus.NO_CONTENT);
        }
        if(userService.isUserExists(user.getUserId())){
            LOGGER.warn("Given user already exists");
            return new ResponseEntity<>
                    ("Cannot save because user with user id : "
                            + user.getUserId() + " already exists", HttpStatus.CONFLICT);   //409
        }
        else{
            LOGGER.info("Successfully inserted "+user+" into User table");
            return ResponseEntity.accepted().body("Successfully registered user: "
                    +userService.register(user).toString());
        }
    }

    @PutMapping("/update") //localhost:8088/update
    @Authorized(allowedRoles = {Role.ADMIN,Role.CUSTOMER,Role.EMPLOYEE})
    public ResponseEntity<String> updateUserInfo(@RequestBody User user){
        authorizationService.guardByUserId(user.getUserId());
        User result = userService.update(user);
        LOGGER.info("Successfully updated "+user);
        return ResponseEntity.accepted().body("Successfully updated user: "+result.toString());
    }

    @PostMapping("/login/{user}/{pass}") //localhost:8088/login/user/pass
    public ResponseEntity<String> login(@PathVariable("user") String username, @PathVariable("pass") String password){
        User u = userService.login(username,password);
        LOGGER.info("Successfully logged in as "+user);
        counter.inc();
        return new ResponseEntity<>("Welcome " + u.getFirstName() + " " + u.getLastName(), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(){
        userService.logout();
        LOGGER.info("Successfully logged out.");
        return ResponseEntity.accepted().body("Successfully logged out");
    }

    @GetMapping("/getusersandcarts")  //localhost:8088/getusersandcarts
    @Authorized(allowedRoles = {Role.ADMIN})
    public ResponseEntity<String> getUsersAndCarts(){
        return ResponseEntity.ok(userService.getUsersAndCarts());
    }

    @GetMapping("/getmycart/{userid}")  // localhost:8088/getmycart/{userid}
    @Authorized(allowedRoles = {Role.ADMIN,Role.CUSTOMER,Role.EMPLOYEE})
    public ResponseEntity<String> getMyCart(@PathVariable("userid") int userId){
        authorizationService.guardByUserId(userId);

        return ResponseEntity.ok(userService.getSingleUserAndCart(userId));
    }

    @PutMapping("/addproducttocart/{userid}/{itemid}") //localhost:8088/addproducttocart/userid/itemId
    @Authorized(allowedRoles = {Role.ADMIN,Role.CUSTOMER,Role.EMPLOYEE})
    public ResponseEntity<String> addProductToCart(@PathVariable("userid") int userId,@PathVariable("itemid") int itemId ){
        authorizationService.guardByUserId(userId);
        ResponseEntity<String> responseEntity;
        user = userService.getUser(userId);
        if(user==null){
            LOGGER.error("Invalid username or password provided");
            responseEntity = new ResponseEntity<>
                    ("Invalid username or password. Try again", HttpStatus.NOT_ACCEPTABLE);
        }
        else{
            result = userService.addItemToCart(user,itemId);

            if(!result){
                LOGGER.warn("Unable to add item to cart");
                responseEntity = new ResponseEntity<>
                        ("Unable to add item to cart", HttpStatus.CONFLICT);
            }
            else{
                LOGGER.info("Successfully added item to cart");
                responseEntity = new ResponseEntity<>
                        ("Successfully added item to cart", HttpStatus.OK);
            }
        }

        return responseEntity;
    }

    @DeleteMapping("/deleteuser/{userid}") //localhost:8088/deleteuser/userid
    @Authorized(allowedRoles = {Role.ADMIN,Role.CUSTOMER,Role.EMPLOYEE})
    public ResponseEntity<String> deleteUser(@PathVariable("userid") int userId){
        authorizationService.guardByUserId(userId);
        ResponseEntity<String> responseEntity;
        if(userService.isUserExists(userId)){
            result = userService.deleteAccount(userId);
            LOGGER.info("Successfully deleted user with userId: "+userId);
            responseEntity = new ResponseEntity<>
                    ("Successfully deleted user", HttpStatus.OK);
        }
        else{
            LOGGER.error("Unable to delete user");
            responseEntity = new ResponseEntity<>
                    ("Unable to delete user because user id: " + userId + " is invalid"
                            , HttpStatus.NOT_ACCEPTABLE);
        }

        return responseEntity;
    }

    @GetMapping("/getitemsinstock") //localhost:8088/getitemsinstock
    public ResponseEntity<String> getItemsInStock(){
        String items = itemService.getAllInstockItems();
        if(Objects.equals(items, "")){
            LOGGER.warn("Item table is empty");
            return new ResponseEntity<>
                    ("No items instock. Please come back later", HttpStatus.NO_CONTENT);
        }
        else{
            LOGGER.info("Fetching all instock items");
            return new ResponseEntity<>(items, HttpStatus.OK);
        }
    }

    @PutMapping("/checkout/{userid}") //localhost:8088/checkout/{userid}
    @Authorized(allowedRoles = {Role.ADMIN,Role.CUSTOMER,Role.EMPLOYEE})
    public ResponseEntity<String> checkout(@PathVariable("userid") int userId){
        authorizationService.guardByUserId(userId);
        ResponseEntity<String> responseEntity;
        if(userService.isUserExists(userId)){
            int total = userService.checkout(userId);
            if(total>0){
                LOGGER.info("Transaction completed");
                responseEntity = new ResponseEntity<>
                        ("Transaction complete.\n" +
                                "Your total today was $" + total +
                                "\nThank you for shopping with us today. Good Bye."
                                , HttpStatus.OK);
            }
            else if(total == 0){
                LOGGER.warn("Nothing currently in user's cart");
                responseEntity = new ResponseEntity<>
                        ("Nothing is in your cart", HttpStatus.NO_CONTENT);
            }
            else{ //total=-1
                LOGGER.warn("No credit card on account to process transaction");
                responseEntity = new ResponseEntity<>
                        ("Unable to process transaction because you do not " +
                                "have a credit card on our account"
                                , HttpStatus.NOT_ACCEPTABLE);
            }
        }
        else{
            LOGGER.error("Unable to process transaction, user does not exist");
            responseEntity = new ResponseEntity<>
                    ("Unable to process transaction because User with user id: "
                            + userId + " does not exist"
                            , HttpStatus.NO_CONTENT);
        }

        return responseEntity;
    }

    @PutMapping("/emptycart/{userid}") //localhost:8088/emptycart/{userid}
    @Authorized(allowedRoles = {Role.ADMIN,Role.CUSTOMER,Role.EMPLOYEE})
    public ResponseEntity<String> emptyCart(@PathVariable("userid") int userId){
        authorizationService.guardByUserId(userId);
        ResponseEntity<String> responseEntity;
        if(userService.isUserExists(userId)){
            result= userService.emptyCart(userId);
            LOGGER.info("Cart successfully emptied");
            responseEntity = new ResponseEntity<>
                    ("Cart successfully emptied"
                            , HttpStatus.OK);
        }
        else{
            LOGGER.error("User with userId: "+userId+" does not exist");
            responseEntity = new ResponseEntity<>
                    ("Unable to process transaction because User with user id: "
                            + userId + " does not exist"
                            , HttpStatus.NO_CONTENT);
        }
        return responseEntity;
    }
}

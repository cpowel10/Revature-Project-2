package com.revature.project1.controller;

import com.revature.project1.model.User;
import com.revature.project1.services.ItemService;
import com.revature.project1.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired()
    User user;

    @Autowired
    UserService userService;

    @Autowired
    ItemService itemService;

    boolean result;

    @PostMapping("/register") //localhost:8088/register
    public ResponseEntity<String> register(@RequestBody User user){
        ResponseEntity responseEntity = null;

        if(userService.isUserExists(user.getUserId())){
            responseEntity = new ResponseEntity<String>
                    ("Cannot save because user with user id : "
                            + user.getUserId() + " already exists", HttpStatus.CONFLICT);   //409
        }
        else{
            result = userService.register(user);
            if(result){
                responseEntity = new ResponseEntity<String>
                        ("Successfully registered user: "+user.getUsername(),HttpStatus.OK);
            }
            else{
                responseEntity = new ResponseEntity<String>
                        ("Failed to save user because user with username: "+user.getUsername()
                                +" or email: "+user.getEmail()+" already exists",HttpStatus.CONFLICT);
            }
        }

        return responseEntity;
    }

    @GetMapping("/login/{user}/{pass}") //localhost:8088/login/user/pass
    public ResponseEntity<String> login(@PathVariable("user") String username, @PathVariable("pass") String password){
        ResponseEntity responseEntity = null;
        User u = userService.login(username,password);
        if(u==null){
            responseEntity = new ResponseEntity("Invalid username or password",HttpStatus.NOT_ACCEPTABLE);
        }
        else{
            responseEntity = new ResponseEntity("Welcome "+u.getFirstName()+" "+u.getLastName(),HttpStatus.OK);
        }
        return responseEntity;
    }

    @GetMapping("/getusersandcarts")  //localhost:8088/getusersandcarts
    public ResponseEntity<String> getUsersAndCarts(){
        ResponseEntity<String> responseEntity = null;
        String userCartString = userService.getUsersAndCarts();
        if(userCartString==""){
            responseEntity = new ResponseEntity<String>(
                    "Something went wrong. Please try again later.",HttpStatus.CONFLICT);
        }
        else{
            responseEntity = new ResponseEntity<String>(userCartString,HttpStatus.OK);
        }
        return responseEntity;
    }

    @PutMapping("/addproducttocart/{user}/{pass}/{itemid}") //localhost:8088/addproducttocart/username/password/itemId
    public ResponseEntity<String> addProductToCart(@PathVariable("user") String username,
                                                   @PathVariable("pass") String password,
                                                   @PathVariable("itemid") int itemId ){
        ResponseEntity<String> responseEntity = null;
        user = userService.login(username,password);
        if(user==null){
            responseEntity = new ResponseEntity<String>
                    ("Invalid username or password. Try again",HttpStatus.NOT_ACCEPTABLE);
        }
        else{
            result = userService.addItemToCart(user,itemId);
            if(!result){
                responseEntity = new ResponseEntity<String>
                        ("Unable to add item to cart",HttpStatus.CONFLICT);
            }
            else{
                System.out.println("Inside UserController: "+user.getCartContents());
                responseEntity = new ResponseEntity<String>
                        ("Successfully added item to cart",HttpStatus.OK);
            }
        }

        return responseEntity;
    }

    @DeleteMapping("/deleteuser/{userid}") //localhost:8088/deleteuser/userid
    public ResponseEntity<String> deleteUser(@PathVariable("userid") int userId){
        ResponseEntity<String> responseEntity = null;
        if(userService.isUserExists(userId)){
            result = userService.deleteAccount(userId);
            responseEntity = new ResponseEntity<String>
                    ("Successfully deleted user",HttpStatus.OK);
        }
        else{
            responseEntity = new ResponseEntity<String>
                    ("Unable to delete user because user id: "+userId+" is invalid"
                            ,HttpStatus.NOT_ACCEPTABLE);
        }

        return responseEntity;
    }

    @GetMapping("/getitemsinstock") //localhost:8088/getitemsinstock
    public ResponseEntity<String> getItemsInStock(){
        ResponseEntity<String> responseEntity = null;
        String items = itemService.getAllInstockItems();
        if(items==""){
            responseEntity = new ResponseEntity<String>
                    ("No items instock. Please come back later",HttpStatus.NO_CONTENT);
        }
        else{
            responseEntity = new ResponseEntity<String>(items,HttpStatus.OK);
        }
        return responseEntity;
    }
}

package com.revature.project1.services;

import com.revature.project1.dao.ItemDAO;
import com.revature.project1.dao.UserDao;
import com.revature.project1.model.Item;
import com.revature.project1.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    UserDao userDAO;

    @Autowired
    ItemDAO itemDAO;

    @Override
    public boolean register(User user) {
        if(userDAO.findByUsername(user.getUsername()).isEmpty() && userDAO.findByEmail(user.getEmail()).isEmpty()){
            userDAO.save(user);
            LOGGER.info("Successfully registered user");
            return true;
        }
        else{
            LOGGER.warn("Failed to save user because user with username: "+user.getUsername()+" or email: "+user.getEmail()+" already exists");
            return false;
        }
    }

    @Override
    public User login(String username, String password) {
        List<User> user = userDAO.findByUsernameAndPassword(username,password);
        if(user.isEmpty()){
            LOGGER.warn("Invalid username or password");
            return null;
        }
        else{
            LOGGER.info("Successfully logged in");
            return user.get(0);
        }
    }

    @Override
    public boolean deleteAccount(int userId) {
        userDAO.deleteById(userId);
        LOGGER.info("Successfully deleted user");
        return true;
    }

    @Override
    public String getUsersAndCarts() {
        String str = "";
        List<User> users = userDAO.findAll(Sort.by("userId"));
        for(User u : users){
            System.out.println("Inside UserServiceImpl(getUsersAndCarts): "+u.getCartContents());
            str=str + u.getFirstName()+" "+u.getLastName()+"'s ("+u.getUsername()+") cart contains: "+u.getCartContents()+"\n";
        }
        return str;
    }

    @Override
    public boolean isUserExists(int userId) {
        return userDAO.existsById(userId);
    }

    @Override
    public boolean addItemToCart(User user, int itemId) {
        Item item = itemDAO.findById(itemId);
        if(item==null){
            return false;
        }
        else{
            user.getCartContents().add(item);
            System.out.println("Inside UserServiceImpl: "+user.getCartContents());
            item.setQoh(item.getQoh()-1);
            //userDAO.saveUserCartContents(user.getUserId(),user.getCartContents());
            userDAO.save(user);
            itemDAO.save(item);
            return true;
        }
    }
}

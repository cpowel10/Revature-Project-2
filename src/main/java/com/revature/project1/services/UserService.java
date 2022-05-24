package com.revature.project1.services;

import com.revature.project1.model.Cart;
import com.revature.project1.model.Item;
import com.revature.project1.model.User;

import java.util.List;

public interface UserService {
    public boolean register(User user);
    public User login(String username, String password);
    public boolean deleteAccount(int userId);
    public String getUsersAndCarts();
    public boolean isUserExists(int userId);
    public boolean addItemToCart(User user,int itemId);
}

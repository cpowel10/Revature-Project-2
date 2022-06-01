package com.revature.project1.services;

import com.revature.project1.model.Cart;
import com.revature.project1.model.Item;
import com.revature.project1.model.User;

import java.util.List;

public interface UserService {
    public User register(User user);
    public User login(String username, String password);
    public void logout();
    public boolean deleteAccount(int userId);
    public String getUsersAndCarts();
    String getSingleUserAndCart(int userId);
    public boolean isUserExists(int userId);
    public boolean addItemToCart(User user,int itemId);
    public User update(User u);
    public User getUser(int userId);
    public int checkout(int userId);
    public boolean emptyCart(int userId);
}

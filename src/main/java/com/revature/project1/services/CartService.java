package com.revature.project1.services;

import com.revature.project1.model.Cart;
import com.revature.project1.model.User;

public interface CartService {
    public Cart getCart(int userId);
}

package com.revature.project1.services;

import com.revature.project1.dao.CartDao;
import com.revature.project1.model.Cart;
import com.revature.project1.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService{
    @Autowired
    CartDao cartDAO;

    @Override
    public Cart getCart(int userId) {
        return cartDAO.getCartFromUserId(userId);
    }
}

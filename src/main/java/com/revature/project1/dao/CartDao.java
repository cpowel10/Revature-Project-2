package com.revature.project1.dao;

import com.revature.project1.model.Cart;
import com.revature.project1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartDao extends JpaRepository<Cart,Integer> {
    @Query("select c from Cart c where userId = ?1")
    public Cart getCartFromUserId(int userId);

    @Query("select c from Cart c where user=?1")
    Cart getCartFromUser(User user);
}

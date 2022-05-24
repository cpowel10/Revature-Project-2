package com.revature.project1.dao;

import com.revature.project1.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartDao extends JpaRepository<Cart,Integer> {
}

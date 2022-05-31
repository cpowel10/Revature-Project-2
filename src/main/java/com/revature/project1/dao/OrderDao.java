package com.revature.project1.dao;

import com.revature.project1.model.Order;
import com.revature.project1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderDao extends JpaRepository<Order,Integer> {
    @Query("select o from Order o where userId = ?1")
    public Order getOrderFromUserId(int userId);

    @Query("select o from Order o where user=?1")
    Order getOrderFromUser(User u);
}

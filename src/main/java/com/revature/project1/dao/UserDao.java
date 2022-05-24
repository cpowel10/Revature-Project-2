package com.revature.project1.dao;

import com.revature.project1.model.Item;
import com.revature.project1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserDao extends JpaRepository<User,Integer> {
    public List<User> findByUsername(String username);
    public List<User> findByUsernameAndPassword(String username, String password);
    public List<User> findByEmail(String email);
    public List<User> findById(int userId);

//    @Override
//    @Query("select u from User u order by userid")
//    public List<User> findAll();

//    @Query("select u from User u")
//    public List<Item> getCartById(int userId);
}

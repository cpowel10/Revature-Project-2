package com.revature.project1.dao;

import com.revature.project1.model.Item;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemDAO extends JpaRepository<Item,Integer> {
    @Query("select i from Item i where qoh > 0")
    public List<Item> getAllInStock(Sort itemId);

    public Item findById(int itemId);


}

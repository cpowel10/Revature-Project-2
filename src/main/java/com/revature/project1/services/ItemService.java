package com.revature.project1.services;

import com.revature.project1.model.Item;

public interface ItemService {
    public boolean addItem(Item item);
    public boolean deleteItem(int itemId);
    public boolean updateItem(Item item);
    public String getAllInstockItems();
    public boolean isItemExists(int itemId);
}

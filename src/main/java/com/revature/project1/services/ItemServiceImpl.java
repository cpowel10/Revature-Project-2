package com.revature.project1.services;

import com.revature.project1.dao.ItemDAO;
import com.revature.project1.model.Item;
import com.revature.project1.model.User;
import com.revature.project1.utilities.CheckNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService{
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemServiceImpl.class);

    @Autowired
    ItemDAO itemDAO;

    @Autowired()
    CheckNumber checkNumber;

    @Override
    public boolean addItem(Item item) {
        if(checkNumber.checkNegativeInt(item.getQoh(),item.getPrice())){
            System.out.println("Item default user: "+item.toString());
            itemDAO.save(item);
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public boolean deleteItem(int itemId) {
        Item i = itemDAO.findById(itemId);
        if(i.getUser() != null){
            User u = i.getUser();
            u.getCartContents().remove(i);
        }
        itemDAO.deleteById(itemId);
        return true;
    }

    @Override
    public boolean updateItem(Item item) {
        if(checkNumber.checkNegativeInt(item.getQoh(),item.getPrice())){
            System.out.println("Item default user: "+item.toString());
            itemDAO.save(item);
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public String getAllInstockItems() {
        List<Item> instock = itemDAO.getAllInStock();
        String str = "";
        for(Item i : instock){
            if(i.getUser() == null){
                str = str + i.getItemId()+": "+i.getItemName()+"-- price: $"+i.getPrice() + "\n";
            }
            else{
                continue;
            }
        }

        return str;
    }

    @Override
    public boolean isItemExists(int itemId) {
        return itemDAO.existsById(itemId);
    }
}

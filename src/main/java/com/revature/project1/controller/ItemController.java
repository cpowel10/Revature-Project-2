package com.revature.project1.controller;

import com.revature.project1.annotations.Authorized;
import com.revature.project1.model.Item;
import com.revature.project1.model.Role;
import com.revature.project1.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ItemController {
    @Autowired
    Item item;

    @Autowired
    ItemService itemService;
    boolean result;

    @PostMapping("/additem") //localhost:8088/additem
    @Authorized(allowedRoles = {Role.ADMIN})
    public ResponseEntity<String> addItem(@RequestBody Item item) {
        ResponseEntity<String> responseEntity = null;

        if (itemService.isItemExists(item.getItemId())) {
            responseEntity = new ResponseEntity<String>
                    ("Cannot save because item with item id :" + item.getItemId() + " already exists", HttpStatus.CONFLICT);
        } else {
            result = itemService.addItem(item);
            if (result) {
                responseEntity = new ResponseEntity<String>
                        ("Successfully Saved your item:" + item.getItemId(), HttpStatus.OK);        //200

            } else {
                responseEntity = new ResponseEntity<String>
                        ("Cannot save item because price or qoh is negative", HttpStatus.NOT_ACCEPTABLE);        //406
            }
        }
        return responseEntity;
    }

    @PostMapping("/deleteitem/{itemid}") //localhost:8088/deleteitem/1
    @Authorized(allowedRoles = {Role.ADMIN})
    public ResponseEntity<String> deleteItem(@PathVariable("itemid") int itemId){
        ResponseEntity<String> responseEntity = null;
        if (!itemService.isItemExists(item.getItemId())) {
            responseEntity = new ResponseEntity<String>
                    ("Cannot delete because item with item id :" + item.getItemId() + " does not exists", HttpStatus.NO_CONTENT);
        }
        else{
            result = itemService.deleteItem(itemId);
            responseEntity = new ResponseEntity<String>
                    ("Successfully deleted the item: " + item.getItemId(), HttpStatus.OK);
        }
        return responseEntity;
    }

    @PutMapping("/updateitem") //localhost:8088/updateitem
    @Authorized(allowedRoles = {Role.ADMIN})
    public ResponseEntity<String> updateItem(@RequestBody Item item){
        ResponseEntity<String> responseEntity = null;
        if (!itemService.isItemExists(item.getItemId())) {
            responseEntity = new ResponseEntity<String>
                    ("Cannot update because item with item :" + item.toString() + " does not exists", HttpStatus.NO_CONTENT);
        }
        else{
            result = itemService.updateItem(item);
            if (result) {
                responseEntity = new ResponseEntity<String>
                        ("Successfully updated your item:" + item.toString(), HttpStatus.OK);        //200

            } else {
                responseEntity = new ResponseEntity<String>
                        ("Cannot update item because price or qoh is negative", HttpStatus.NOT_ACCEPTABLE);        //406
            }
        }
        return responseEntity;
    }
}
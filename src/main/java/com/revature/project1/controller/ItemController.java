package com.revature.project1.controller;

import com.revature.project1.model.Item;
import com.revature.project1.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemController {
    @Autowired
    Item item;

    @Autowired
    ItemService itemService;
    boolean result;

    @PostMapping("/additem") //localhost:8088/additem
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
                        ("Cannot save because item because price or qoh is negative", HttpStatus.NOT_ACCEPTABLE);        //406
            }
        }
        return responseEntity;
    }
}
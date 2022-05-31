package com.revature.project1.services;

import com.revature.project1.dao.CartDao;
import com.revature.project1.dao.ItemDAO;
import com.revature.project1.dao.OrderDao;
import com.revature.project1.dao.UserDao;
import com.revature.project1.exceptions.UserNotFoundException;
import com.revature.project1.model.Cart;
import com.revature.project1.model.Item;
import com.revature.project1.model.Order;
import com.revature.project1.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    UserDao userDAO;

    @Autowired
    ItemDAO itemDAO;

    @Autowired
    OrderDao orderDAO;

    @Autowired
    CartDao cartDAO;

    @Autowired
    private HttpServletRequest req;

    @Override
    public User register(User user) {
        if(userDAO.findByUsername(user.getUsername())==null){
            Order o = new Order();
            Cart c = new Cart();
            o.setUserId(user.getUserId());
            c.setUserId(user.getUserId());
            userDAO.save(user);
            cartDAO.save(c);
            orderDAO.save(o);
            return user;
        }
        else{
            throw new RuntimeException("Username already exists.");
        }
    }

    @Override
    public User update(User user){
        if(!userDAO.existsById(user.getUserId())){
            throw new RuntimeException
                    ("Failed to update user info because " +
                            "given user does not already exist");
        }

        userDAO.save(user);
        HttpSession session = req.getSession(false);

        User sessionUser = (User) session.getAttribute("currentUser");
        if(sessionUser.getUserId() == user.getUserId()){
            session.setAttribute("currentUser",user);
        }
        return user;
    }

    @Override
    public User login(String username, String password) {
        User exists = userDAO.findByUsernameAndPassword(username,password)
                .orElseThrow(() -> new UserNotFoundException(String.format
                        ("username: %s does not exist",username)));

        HttpSession session = req.getSession();
        session.setAttribute("currentUser",exists);
        return exists;
    }

    @Override
    public void logout(){
        HttpSession session = req.getSession(false);
        if(session == null){
            return;
        }
        session.invalidate();
    }

    @Override
    public boolean deleteAccount(int userId) {
        userDAO.deleteById(userId);
        return true;
    }

    @Override
    public String getUsersAndCarts() {
        String str = "";
        List<User> users = userDAO.findAll(Sort.by("userId"));
        for(User u : users){
            //Order order = orderDAO.getOrderFromUserId(u.getUserId());
            str=str + u.getFirstName()+" "+u.getLastName()+"'s ("+u.getUsername()+") cart contains:\n";
            int totalPrice=0;
            for(Item i : u.getCartContents()){
                str=str+i.getItemName()+" -- $"+i.getPrice()+"\n";
                totalPrice = totalPrice+i.getPrice();
            }
            str=str+"total= $"+totalPrice+"\n--------------------------------\n";
            //str=str+"total Price: $"+order.getTotalPrice()
            //        +"\n--------------------------------\n";
        }
        return str;
    }

    @Override
    public String getSingleUserAndCart(int userId){
        User user = userDAO.findById(userId);
        //Order order = orderDAO.getOrderFromUserId(userId);
//        Cart cart = cartDAO.getCartFromUserId(user.getUserId());
        String str = user.getFirstName()+" "+user.getLastName()+"'s ("+user.getUsername()+") cart contains:\n";
        int totalPrice=0;
        for(Item i : user.getCartContents()){
            str=str+i.getItemName()+" -- $"+i.getPrice()+"\n";
            totalPrice = totalPrice+i.getPrice();
        }
        str=str+"total= $"+totalPrice+"\n--------------------------------\n";
//        str=str+"total Price: $"+order.getTotalPrice()
//                +"\n--------------------------------\n";
        return str;
    }

    @Override
    public boolean isUserExists(int userId) {
        return userDAO.existsById(userId);
    }

    @Override
    @Transactional
    public boolean addItemToCart(User user, int itemId) {
        Item item = itemDAO.findById(itemId);
        Cart cart = cartDAO.getCartFromUserId(user.getUserId());
        if(item==null){
            return false;
        }
        else{
            System.out.println("Cart Contents before add: "+user.getCartContents());
            user.getCartContents().add(item);
            System.out.println("Cart Contents after add: "+user.getCartContents());
            System.out.println("Item info before set: "+item.toString());
            item.setQoh(item.getQoh()-1);
            item.setUser(user);
            System.out.println("Item info after set: "+item.toString());
            cart.setNumItemsInCart(cart.getNumItemsInCart()+1);
            Order order = orderDAO.getOrderFromUserId(user.getUserId());
            order.setTotalPrice(order.getTotalPrice()+item.getPrice());
            System.out.println("Cart Contents before save: "+user.getCartContents());
            userDAO.save(user);
            itemDAO.save(item);
            cartDAO.save(cart);
            orderDAO.save(order);
            System.out.println("Cart Contents after save: "+user.getCartContents());
            return true;
        }
    }

    @Override
    public User getUser(int userId){
        return userDAO.findById(userId);
    }
}

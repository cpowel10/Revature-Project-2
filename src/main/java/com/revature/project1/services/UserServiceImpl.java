package com.revature.project1.services;

import com.revature.project1.dao.CartDao;
import com.revature.project1.dao.ItemDAO;
import com.revature.project1.dao.OrderDao;
import com.revature.project1.dao.UserDao;
import com.revature.project1.exceptions.UserNotFoundException;
import com.revature.project1.model.*;
//import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.prometheus.client.Counter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{
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

    @Autowired
    private MeterRegistry meterRegistry;

    static final Counter adminCounter = Counter.build()
            .name("total_admin_users")
            .help("Total number of ADMIN users created")
            .register();
    private Counter employeeCounter = Counter.build()
            .name("total_employee_users")
            .help("Total number of EMPLOYEE users created")
            .register();
    private Counter customerCounter = Counter.build()
            .name("total_customers_users")
            .help("Total number of CUSTOMER users created")
            .register();

    @Override
    public User register(User user) {
        if(userDAO.findByUsername(user.getUsername())==null){
            Order o = new Order();
            Cart c = new Cart();
            o.setUserId(user.getUserId());
            c.setUserId(user.getUserId());
            if(user.getRole() == Role.ADMIN){
                adminCounter.inc(1.0);
            }
            else if(user.getRole() == Role.EMPLOYEE){
                employeeCounter.inc(1.0);
            }
            else{
                customerCounter.inc(1.0);
            }
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
        User u = userDAO.findById(userId);
        List<Item> items = new ArrayList<>(u.getCartContents());
        if(!items.isEmpty()){
            for(Item i : items){
                i.setUser(null);
                i.setQoh(i.getQoh()+1);
                itemDAO.save(i);
            }
        }
        userDAO.deleteById(userId);
        return true;

    }

    @Override
    public String getUsersAndCarts() {
        String str = "";
        List<User> users = userDAO.findAll(Sort.by("userId"));
        for(User u : users){
            Order o = orderDAO.getOrderFromUserId(u.getUserId());
            str=str + u.getFirstName()+" "+u.getLastName()+"'s ("+u.getUsername()+") cart contains:\n";
            for(Item i : u.getCartContents()){
                str=str+i.getItemName()+" -- $"+i.getPrice()+"\n";
            }
            str=str+"total= $"+o.getTotalPrice()+"\n--------------------------------\n";
        }
        return str;
    }

    @Override
    public String getSingleUserAndCart(int userId){
        User user = userDAO.findById(userId);
        Order o = orderDAO.getOrderFromUserId(userId);
        String str = user.getFirstName()+" "+user.getLastName()+"'s ("+user.getUsername()+") cart contains:\n";
        for(Item i : user.getCartContents()){
            str=str+i.getItemName()+" -- $"+i.getPrice()+"\n";
        }
        str=str+"total= $"+o.getTotalPrice()+"\n--------------------------------\n";
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
            user.getCartContents().add(item);
            item.setQoh(item.getQoh()-1);
            item.setUser(user);
            cart.setNumItemsInCart(cart.getNumItemsInCart()+1);
            Order order = orderDAO.getOrderFromUserId(user.getUserId());
            order.setTotalPrice(order.getTotalPrice()+item.getPrice());
            userDAO.save(user);
            itemDAO.save(item);
            cartDAO.save(cart);
            orderDAO.save(order);
            return true;
        }
    }

    @Override
    public User getUser(int userId){
        return userDAO.findById(userId);
    }

    @Override
    public int checkout(int userId){
        User u = userDAO.findById(userId);
        if(u.getCardNum() == null){
            return -1;
        }
        if(u.getCartContents().isEmpty()){
            return 0;
        }
        Cart c = cartDAO.getCartFromUserId(userId);
        Order o = orderDAO.getOrderFromUserId(userId);
        int total = o.getTotalPrice();
        List<Item> items = new ArrayList<>(u.getCartContents());
        for(Item i : items){
            u.getCartContents().remove(i);
            itemDAO.deleteById(i.getItemId());
        }
        c.setNumItemsInCart(0);
        o.setTotalPrice(0);
        userDAO.save(u);
        cartDAO.save(c);
        orderDAO.save(o);
        return total;
    }

    @Override
    public boolean emptyCart(int userId) {
        User u = userDAO.findById(userId);
        if(u.getCartContents().isEmpty()){
            return true;
        }
        Cart c = cartDAO.getCartFromUserId(userId);
        Order o = orderDAO.getOrderFromUserId(userId);
        List<Item> items = new ArrayList<>(u.getCartContents());
        for(Item i : items){
            u.getCartContents().remove(i);
            i.setUser(null);
            i.setQoh(i.getQoh()+1);
            itemDAO.save(i);
        }
        c.setNumItemsInCart(0);
        o.setTotalPrice(0);
        userDAO.save(u);
        cartDAO.save(c);
        orderDAO.save(o);
        return true;
    }
}

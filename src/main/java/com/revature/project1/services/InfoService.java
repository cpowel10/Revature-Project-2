package com.revature.project1.services;

import com.revature.project1.dao.UserDao;
import com.revature.project1.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class InfoService implements InfoContributor {
    @Autowired
    UserDao userDao;

    @Override
    public void contribute(Info.Builder builder) {
        HashMap<String,Integer> userCount = new HashMap<>();
        List<User> users = userDao.findAll();
        for(User u : users){
            userCount.put(u.getFirstName(),u.getUserId());
        }
        builder.withDetail("userMetrics", userCount);
    }
}

package com.revature.project1.services;

import com.revature.project1.dao.UserDao;
import com.revature.project1.model.User;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class InfoService implements InfoContributor {
//    @Autowired
//    private UserDao userDao;
//
//    private MeterRegistry meterRegistry;
//
//    public InfoService(MeterRegistry meterRegistry){
//        this.meterRegistry = meterRegistry;
//    }
//
    @Override
    public void contribute(Info.Builder builder) {
        //Counter userCounter = this.meterRegistry.counter("users","role","ADMIN");
    }
}

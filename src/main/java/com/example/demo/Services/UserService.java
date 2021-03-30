package com.example.demo.Services;

import com.example.demo.DAO.KPIUserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private KPIUserDAO kpiUserDAO;

    public boolean isUser(String username, String password){
        return kpiUserDAO.isUser(username, password);
    }

}

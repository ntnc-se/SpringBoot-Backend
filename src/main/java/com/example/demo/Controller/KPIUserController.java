package com.example.demo.Controller;

import com.example.demo.Model.KPIUser;
import com.example.demo.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController

public class KPIUserController {

    @Autowired
    private UserService userService;

    // user request data in json format
    // value stand for url call
    // produces can generate data in json format as per what client request

    @PostMapping(value = "/api/login", consumes = "application/json", produces = "application/json")
    public boolean isUser(@RequestBody KPIUser kpiUser){
        return userService.isUser(kpiUser.getUsername(), kpiUser.getPassword());
    }

}

package com.example.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {
    // Match everything without a suffix (so not a static resource)
    @RequestMapping(value = "/home/**")
    public String redirectGoalPath() {
        // Forward to home page so that route is preserved.(i.e forward:/intex.html)
        return "forward:/";
    }

    @RequestMapping(value = "/hoachtoan/**")
    public String redirectFinalPath() {
        // Forward to home page so that route is preserved.(i.e forward:/intex.html)
        return "forward:/";
    }


}

package com.commerce.content.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {


    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping(value = "/signup")
    public String signup(){
        return "signup";
    }
}

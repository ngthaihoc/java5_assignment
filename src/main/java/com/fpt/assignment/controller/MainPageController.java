package com.fpt.assignment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

public class MainPageController {


    @RequestMapping("/synkrokbooks")
    public String mainPage(){
        return  "views/main";
    }
}

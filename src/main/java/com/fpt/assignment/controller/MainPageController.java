package com.fpt.assignment.controller;

import com.fpt.assignment.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

public class MainPageController {

    @Autowired
    BookRepository bookRepo;

    @RequestMapping("/home")
    public String mainPage(Model model) {

        model.addAttribute("topBooks", bookRepo.findAll(PageRequest.of(0, 8)).getContent());
        return "/components/content";
    }
}

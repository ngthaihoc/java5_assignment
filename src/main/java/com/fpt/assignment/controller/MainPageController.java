package com.fpt.assignment.controller;

import com.fpt.assignment.entity.Book;
import com.fpt.assignment.repository.BookRepository;
import com.fpt.assignment.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller

public class MainPageController {

    @Autowired
    BookRepository bookRepo;

    @Autowired
    CategoryRepository categoryRepo;

    @RequestMapping("/home")
    public String mainPage(Model model, @RequestParam("cateid") Optional<Long> cateid) {
//      Top sách
        model.addAttribute("topBooks", bookRepo.findAll(PageRequest.of(0, 8)).getContent());

//      thể loại
        model.addAttribute("categories", categoryRepo.findAll());

        List<Book> list;
        if (cateid.isPresent()) {
            list = list = bookRepo.findByCategoryId(cateid.get(), PageRequest.of(0, 8));
        } else {
            list = bookRepo.findAll(PageRequest.of(0, 8)).getContent();
        }


        model.addAttribute("booksForYou", list);
        return "/components/content";
    }
}

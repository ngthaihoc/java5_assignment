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
        // Top sách (chỉ lấy sách available)
        model.addAttribute("topBooks", bookRepo.findAllByOrderByPublishDateDesc(PageRequest.of(0, 8)));

        // thể loại
        model.addAttribute("categories", categoryRepo.findAll());

        List<Book> list;
        if (cateid.isPresent()) {
            list = bookRepo.findByCategoryId(cateid.get(), PageRequest.of(0, 8));
        } else {
            list = bookRepo.findAll(PageRequest.of(0, 8)).getContent();
        }

        // Sách mới
        List<Book> newestList = bookRepo.findAllByOrderByPublishDateDesc(PageRequest.of(0, 5));
        if (!newestList.isEmpty()) {
            model.addAttribute("lastestBook", newestList.get(0));
        }
        if (newestList.size() > 1) {
            model.addAttribute("relatedNewBooks", newestList.subList(1, newestList.size()));
        }

        model.addAttribute("booksForYou", list);
        return "/components/content";
    }
}

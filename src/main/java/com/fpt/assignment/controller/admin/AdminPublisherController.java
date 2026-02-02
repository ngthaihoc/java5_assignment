package com.fpt.assignment.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fpt.assignment.entity.Publisher;
import com.fpt.assignment.repository.BookRepository;
import com.fpt.assignment.repository.PublisherRepository;

@Controller
@RequestMapping("/admin/publishers")
public class AdminPublisherController {
    @Autowired
    private PublisherRepository publisherRepository;
    @Autowired
    private BookRepository bookRepository;

    @GetMapping
    public String publishers(Model model) {
        model.addAttribute("active", "categories");
        model.addAttribute("publishers", publisherRepository.findAll());
        return "views/admin/publishers";
    }

    @PostMapping("/create")
    public String createPublisher(@ModelAttribute Publisher publisher) {
        publisherRepository.save(publisher);
        return "redirect:/admin/publishers";
    }

    @PostMapping("/update")
    public String updatePublisher(@ModelAttribute Publisher publisher) {
        publisherRepository.save(publisher);
        return "redirect:/admin/publishers";
    }

    @GetMapping("/delete/{id}")
    public String deletePublisher(@PathVariable("id") Long id, RedirectAttributes ra) {
        if(bookRepository.existsByPublisher_Id(id)) {
            ra.addFlashAttribute("error", "Không thể xóa NXB vì vẫn còn sách!");
            return "redirect:/admin/publishers";
        }
        publisherRepository.deleteById(id);
        ra.addFlashAttribute("success", "Xóa nhà xuất bản thành công!");
        
        return "redirect:/admin/publishers";
    }   
}

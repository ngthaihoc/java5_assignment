package com.fpt.assignment.controller.admin;

import com.fpt.assignment.entity.Book;
import com.fpt.assignment.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/books")
@RequiredArgsConstructor
public class AdminBookController {
    final BookRepository bookRepository;
    final CategoryRepository categoryRepository;
    final PublisherRepository publisherRepository;
    final OrderRepository orderRepository;
    final OrderDetailRepository orderDetailRepository;

    @GetMapping
    public String books(Model model) {
        model.addAttribute("active", "books");
        model.addAttribute("books", bookRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("publishers", publisherRepository.findAll());
        return "views/admin/books";
    }

    @PostMapping("/create")
    public String createBook(@ModelAttribute Book book, RedirectAttributes redirectAttributes) {
        try {
            bookRepository.save(book);
            redirectAttributes.addFlashAttribute("success", "Thêm sách thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/books";
    }

    @PostMapping("/update")
    public String updateBook(@ModelAttribute Book book, RedirectAttributes redirectAttributes) {
        try {
            bookRepository.save(book);
            redirectAttributes.addFlashAttribute("success", "Cập nhật thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi cập nhật: " + e.getMessage());
        }
        return "redirect:/admin/books";
    }

    @GetMapping("/delete/{id}")
    @Transactional
    public String deleteBook(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            if (!bookRepository.existsById(id)) {
                redirectAttributes.addFlashAttribute("error", "Sách không tồn tại!");
                return "redirect:/admin/books";
            }


            List<Long> orderIds = orderDetailRepository.findOrderIdsByBookId(id);


            if (!orderIds.isEmpty()) {
                orderIds.forEach(orderId -> orderRepository.deleteById(orderId));
            }

            bookRepository.deleteById(id);

            if (!orderIds.isEmpty()) {
                redirectAttributes.addFlashAttribute("success",
                        "Đã xóa sách và " + orderIds.size() + " đơn hàng liên quan!");
            } else {
                redirectAttributes.addFlashAttribute("success", "Xóa sách thành công!");
            }

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Không thể xóa sách: " + e.getMessage());
        }
        return "redirect:/admin/books";
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public Book getBook(@PathVariable("id") Long id) {
        return bookRepository.findById(id).orElse(null);
    }
}
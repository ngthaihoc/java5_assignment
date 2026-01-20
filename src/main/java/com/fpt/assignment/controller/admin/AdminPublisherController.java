package com.fpt.assignment.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/publishers")
public class AdminPublisherController {

    @GetMapping
    public String publishers(Model model) {

        model.addAttribute("active", "publishers");

        model.addAttribute("publishers", List.of(
                Map.of(
                        "id", 1,
                        "name", "Lioncrest Publishing",
                        "address", "Tennessee, USA",
                        "email", "support@lioncrest.com"
                ),
                Map.of(
                        "id", 2,
                        "name", "Beacon Press",
                        "address", "Boston, USA",
                        "email", "info@beacon.org"
                )
        ));

        return "views/admin/publishers";
    }
}

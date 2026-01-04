package com.doctor.file_processor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @GetMapping("/asdf")
    public String home(Model model) {
        model.addAttribute("so", "gdfgdf");
        return "index";
    }
}

package com.sd.urlshortener;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HelpController {

    @GetMapping("/help")
    public String helpPage(HttpServletRequest request, Model model) {
        String baseUrl = request.getRequestURL().substring(0, request.getRequestURL().length() - 4);
        model.addAttribute("baseUrl", baseUrl);
        return "helpPage";
    }
    
}
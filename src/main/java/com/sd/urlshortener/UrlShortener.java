package com.sd.urlshortener;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RequestMapping("/")
@RestController
public class UrlShortener {

    int counter = 0;

    @GetMapping("/{id}")
    public RedirectView redirectToLongUrl(@PathVariable String id, HttpServletRequest req, HttpServletResponse res) {
        String longUrl = "/";
        RedirectView redirectView = new RedirectView();

        if(id.equals("stefan")) {
            longUrl = "https://stefandunic.com";
            redirectView.setStatusCode(HttpStatus.PERMANENT_REDIRECT);
        }
        else {
            redirectView.setStatusCode(HttpStatus.NOT_FOUND);
        }
        redirectView.setUrl(longUrl);

        return redirectView;
    }
}
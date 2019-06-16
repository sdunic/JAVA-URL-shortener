package com.sd.urlshortener;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/")
@RestController
public class UrlShortener {

    int counter = 0;

    @GetMapping("/{id}")
    public Integer getUrl(@PathVariable String id) {
        System.out.println("Shortened URL: " + id);
        
        counter++;

        return counter;
    }
}
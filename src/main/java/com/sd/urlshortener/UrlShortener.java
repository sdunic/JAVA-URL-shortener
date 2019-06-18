package com.sd.urlshortener;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RequestMapping("/")
@RestController
public class UrlShortener {

    int counter = 0;

    HashMap<String, ShortURL> urls = new HashMap<String, ShortURL>();


    @PostMapping("/register")
    public JSONObject createShortUrl(@RequestBody String obj, HttpServletRequest request) throws ParseException {
        JSONObject output = new JSONObject();

        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(obj);

        ShortURL s = new ShortURL(json.get("url").toString(), ++counter);
        if(json.get("redirectType") != null) {
            s.redirectType = json.get("redirectType").toString();
        }
        urls.put(s.shortUrl, s);

        System.out.println(urls);
        
        String outputUrl = request.getRequestURL().subSequence(0, request.getRequestURL().length() - 8) + s.shortUrl;
        output.put("shortUrl", outputUrl);

        return output;
    }

    @GetMapping("/{id}")
    public RedirectView redirectToLongUrl(@PathVariable String id) {
        RedirectView redirectView = new RedirectView();
        String longUrl = "/";
        ShortURL s = urls.get(id);
        if(s != null) {
            longUrl = s.longUrl;
            if(s.redirectType.equals("301"))
                redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
            else
                redirectView.setStatusCode(HttpStatus.MOVED_TEMPORARILY);

        }
        else {
            redirectView.setStatusCode(HttpStatus.NOT_FOUND);
        }
        redirectView.setUrl(longUrl);
        return redirectView;
    }
}
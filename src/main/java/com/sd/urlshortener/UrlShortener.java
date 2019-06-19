package com.sd.urlshortener;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RequestMapping("/")
@RestController
public class UrlShortener {

    int counter = 0;

    HashMap<String, ShortURL> urls = new HashMap<String, ShortURL>();
    HashMap<String, Account> accounts = new HashMap<String, Account>();
    HashMap<String, String> authorizationCodes = new HashMap<String, String>();


    @PostMapping("/account")
    public JSONObject createAccount(@RequestBody JSONObject json, HttpServletResponse response) {
        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);

        JSONObject output = new JSONObject();

        if(accounts.get(json.get("AccountId")) == null)
        {
            Account account = new Account(json.get("AccountId").toString());
            accounts.put(account.accountId, account);
            authorizationCodes.put(account.getAuthorization(), account.accountId);

            output.put("success", true);
            output.put("description", "Your account is opened");
            output.put("password", account.password);
            response.setStatus(200);
        } 
        else 
        {
            output.put("success", false);
            output.put("description", "Account already exists");
            response.setStatus(200);  
        }

        response.setContentType("application/json");
        System.out.println(accounts);
        return output;
    }

    @PostMapping("/register")
    public JSONObject createShortUrl(@RequestHeader String Authorization, @RequestBody JSONObject json, HttpServletRequest request, HttpServletResponse response) {
        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);

        JSONObject output = new JSONObject();

        String authorizationCode = Authorization.split(" ")[1];
        String accountId = authorizationCodes.get(authorizationCode);
        Account account = accounts.get(accountId);

        if(account != null) {
            String longUrl = json.get("url").toString();

            ShortURL s = new ShortURL(longUrl, accountId, ++counter);
            account.addUrl(longUrl);

            if(json.get("redirectType") != null) {
                s.redirectType = json.get("redirectType").toString();
            }
            urls.put(s.shortUrl, s);
            
            String outputUrl = request.getRequestURL().subSequence(0, request.getRequestURL().length() - 8) + s.shortUrl;
            output.put("shortUrl", outputUrl);
            
            System.out.println(urls);
            response.setStatus(200);
        }
        else {
            response.setStatus(401);
        }

        response.setContentType("application/json");
        return output;
    }

    @GetMapping("statistic/{AccountId}")
    public JSONObject getAccountStatistics(@RequestHeader String Authorization, @PathVariable String AccountId, HttpServletResponse response) {
        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);

        JSONObject output = new JSONObject();
   
        String authorizationCode = Authorization.split(" ")[1];
        String accountId = authorizationCodes.get(authorizationCode);
        Account account = accounts.get(accountId);

        if(account != null) {
            output.putAll(account.urlStatistics);
            response.setStatus(200);
        }
        else {
            response.setStatus(401);
        }

        response.setContentType("application/json");
        return output;        
    }

    @GetMapping("/{id}")
    public RedirectView redirectToLongUrl(@PathVariable String id) {
        RedirectView redirectView = new RedirectView();
        String longUrl = "/";
        String accountId;
        ShortURL s = urls.get(id);
        if(s != null) {
            longUrl = s.longUrl;
            accountId = s.accountId;
            accounts.get(accountId).addVisit(longUrl);

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
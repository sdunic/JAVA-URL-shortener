package com.sd.urlshortener;

import java.util.Base64;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class UrlShortenerController {

    int counter = 0;

    HashMap<String, ShortURL> urls = new HashMap<String, ShortURL>();
    HashMap<String, Account> accounts = new HashMap<String, Account>();

    private String decodeAuthorization(String authorizationCode) {
        String usernamePassword;

        byte[] decodedBytes = Base64.getDecoder().decode(authorizationCode);
        usernamePassword = new String(decodedBytes);

        System.out.println(usernamePassword);
        return usernamePassword;
    }

    @PostMapping(value = "/account", consumes = "application/json", produces = "application/json")
    public JSONObject createAccount(@RequestBody JSONObject json, HttpServletResponse response) {
        JSONObject output = new JSONObject();

        if (accounts.get(json.get("AccountId")) == null) {
            Account account = new Account(json.get("AccountId").toString());
            accounts.put(account.accountId, account);

            output.put("success", true);
            output.put("description", "Your account is opened");
            output.put("password", account.password);
        } else {
            output.put("success", false);
            output.put("description", "Account already exists");
        }

        response.setStatus(200);
        System.out.println(accounts);
        return output;
    }

    @PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
    public JSONObject createShortUrl(@RequestHeader String Authorization, @RequestBody JSONObject json,
            HttpServletRequest request, HttpServletResponse response) {
        JSONObject output = new JSONObject();

        String authorizationCode = Authorization.split(" ")[1];
        String usernamePassword = decodeAuthorization(authorizationCode);
        Account account = accounts.get(usernamePassword.split(":")[0]);

        if (account != null && account.password.equals(usernamePassword.split(":")[1])) {
            String longUrl = json.get("url").toString();

            ShortURL s = new ShortURL(longUrl, account.accountId, ++counter);
            account.addUrl(longUrl);

            if (json.get("redirectType") != null) {
                s.redirectType = json.get("redirectType").toString();
            }
            urls.put(s.shortUrl, s);

            String outputUrl = request.getRequestURL().subSequence(0, request.getRequestURL().length() - 8)
                    + s.shortUrl;
            output.put("shortUrl", outputUrl);

            System.out.println(urls);
            response.setStatus(200);
        } else {
            response.setStatus(401);
        }

        return output;
    }

    @GetMapping(value = "/statistic/{AccountId}", produces = "application/json")
    public JSONObject getAccountStatistics(@RequestHeader String Authorization, @PathVariable String AccountId,
            HttpServletResponse response) {
        JSONObject output = new JSONObject();

        String authorizationCode = Authorization.split(" ")[1];
        String usernamePassword = decodeAuthorization(authorizationCode);
        Account account = accounts.get(usernamePassword.split(":")[0]);

        if (account != null && account.password.equals(usernamePassword.split(":")[1])) {
            output.putAll(account.urlStatistics);

            System.out.println(account.urlStatistics);
            response.setStatus(200);
        } else {
            response.setStatus(401);
        }

        return output;
    }

    @GetMapping("/{id}")
    public RedirectView redirectToLongUrl(@PathVariable String id) {
        RedirectView redirectView = new RedirectView();
        String longUrl = "/";
        String accountId;
        ShortURL s = urls.get(id);
        if (s != null) {
            longUrl = s.longUrl;
            accountId = s.accountId;
            accounts.get(accountId).addVisit(longUrl);

            if (s.redirectType.equals("301"))
                redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
            else
                redirectView.setStatusCode(HttpStatus.MOVED_TEMPORARILY);

        } else {
            redirectView.setStatusCode(HttpStatus.NOT_FOUND);
        }
        redirectView.setUrl(longUrl);
        return redirectView;
    }
}
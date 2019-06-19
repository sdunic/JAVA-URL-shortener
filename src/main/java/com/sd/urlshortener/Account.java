package com.sd.urlshortener;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;

public class Account {

    String accountId;
    String password;
    public HashMap<String, Integer> urlStatistics = new HashMap<String, Integer>();

    public Account(String accountId) {
        this.accountId = accountId;
        this.password = generateRandomPassword();
    }

    public String generateRandomPassword(){
        final String characters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        SecureRandom random = new SecureRandom();
        
        String result="";
        for(int i = 0; i<8; i++){
            int index = random.nextInt(characters.length());
            result += characters.charAt(index);
        }

        return result;
    }

    public void addUrl(String longUrl) {
        urlStatistics.put(longUrl, 0);
    }

    public void addVisit(String longUrl) {
        urlStatistics.put(longUrl, urlStatistics.get(longUrl) + 1);
    }

    public String getStatistics() {
        return this.urlStatistics.toString();
    }

    public String getAuthorization() {
        String originalInput = accountId + ":" + password;
        String encodedString = Base64.getEncoder().encodeToString(originalInput.getBytes());
        
        return encodedString;
    }

    public String toString() { 
        return "accountId: " + this.accountId + ", password: " + this.password + ", statistics: " + this.urlStatistics;
    } 
}  
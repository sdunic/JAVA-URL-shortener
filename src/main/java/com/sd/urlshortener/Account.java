package com.sd.urlshortener;

import java.util.HashMap;

public class Account {

    String password;
    public HashMap<String, Integer> urls = new HashMap<String, Integer>();


    public Account() {
        this.password = generateRandomPassword();
    }

    public String generateRandomPassword(){
        return "";
    }

    public String toString() { 
        return "";
    } 
}  
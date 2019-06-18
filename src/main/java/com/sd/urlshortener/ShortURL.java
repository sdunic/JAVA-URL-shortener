package com.sd.urlshortener;

public class ShortURL {

    public String redirectType = "302";
    String longUrl = "";
    String accountId = "";

    public ShortURL(String longUrl) {
        this.longUrl = longUrl;
    }

    public String toString() { 
        return "redirectType: '" + this.redirectType + "', longUrl: '" + this.longUrl + "', accountId: '" + this.accountId + "'";
    } 
}  
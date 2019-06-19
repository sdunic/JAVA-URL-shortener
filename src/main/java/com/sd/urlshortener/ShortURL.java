package com.sd.urlshortener;

public class ShortURL {

    public String redirectType = "302";
    String longUrl = "";
    String shortUrl = "";
    String accountId = "";

    public ShortURL(String longUrl, String accountId, Integer i) {
        this.longUrl = longUrl;
        this.accountId = accountId;
        this.shortUrl = base62_encode(i);
    }

    public String toString() { 
        return "shortURL: " + this.shortUrl + ", longURL: " + this.longUrl + ", redirectType: " + this.redirectType;
    }

    private String base62_encode(Integer i){
        String[]  s = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

        String output = "";
        while (i > 0) {
            output = s[i % 62] + output;
            i = i/62;
        }
        return output;
    }
}  
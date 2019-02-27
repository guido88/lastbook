package com.example.acer.lastbook;

/**
 * Created by acer on 06/10/2016.
 */
public class Review {

    private String autore;
    private String commento;

    public String getCommento() {
        return commento;
    }

    public void setCommento(String commento) {
        this.commento = commento;
    }

    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public Review(String autore, String commento){

        this.autore=autore;
        this.commento=commento;

    }
}

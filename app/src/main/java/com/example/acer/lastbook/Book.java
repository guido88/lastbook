package com.example.acer.lastbook;

import android.graphics.drawable.Drawable;

/**
 * Created by acer on 09/07/2016.
 */
public class Book {

    private String editore;
    private String titolo;
    private String link;
    private Drawable immagine;
    private String urlImg;

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    public Book(String editore, String titolo, String link, Drawable immagine, String urlimg) {

        this.editore=editore;
        this.titolo = titolo;
        this.immagine = immagine;
        this.link=link;
        this.urlImg=urlimg;
    }

    public String getEditore() {
        return editore;
    }

    public void setEditore(String editore) {
        this.editore = editore;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Drawable getImmagine() {
        return immagine;
    }

    public void setImmagine(Drawable immagine) {
        this.immagine = immagine;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }




}

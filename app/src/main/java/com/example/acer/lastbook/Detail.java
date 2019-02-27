package com.example.acer.lastbook;

import android.graphics.drawable.Drawable;

/**
 * Created by acer on 21/08/2016.
 */
public class Detail {

    private String titolo;
    private String descrizione;
    private String isbn;
    private String autore;
    private String prezzo;
    private Drawable img;

    public Detail(String titolo, String autore, String descrizione, String isbn, String prezzo,Drawable img){

        this.titolo=titolo;
        this.autore=autore;
        this.descrizione=descrizione;
        this.isbn=isbn;
        this.prezzo=prezzo;
        this.img=img;

    }
    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(String prezzo) {
        this.prezzo = prezzo;
    }

    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public Drawable getImg() {
        return img;
    }

    public void setImg(Drawable img) {
        this.img = img;
    }
}

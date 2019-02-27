package com.example.acer.lastbook;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class LastBook extends AppCompatActivity {


    private Book[] books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_book);

        Toolbar toolbar = (Toolbar) findViewById(R.id.mytoolbarbooks);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        ListView lista = (ListView) findViewById(R.id.list);
        new GetList(this,lista).execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menuspinner, menu);
        MenuItem item = menu.findItem(R.id.spinner1);

        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);

        Context b=this.getSupportActionBar().getThemedContext();
        SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(b,R.array.Editori,android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(mSpinnerAdapter);
        return true;
    }

    class GetList extends AsyncTask<Void,Void,Void> {

        Context context;
        private ListView listview;
        private ProgressDialog bar;

        public GetList(Context context,ListView view){
            this.listview=view;
            this.context=context;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            bar.dismiss();
            this.listview.setAdapter(new BookAdapter(this.context,books,getWindowManager()));
            this.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                        String link = books[position].getLink();
                        String editore = books[position].getEditore();
                        String img = books[position].getUrlImg();

                        Intent i1 = new Intent(LastBook.this,Book_Details.class);
                        i1.putExtra("link",link);
                        i1.putExtra("editore",editore);
                        i1.putExtra("img",img);
                        if(Utility.isOnline())
                        startActivity(i1);
                        else
                            Toast.makeText(getApplicationContext(), "Errore di connessione", Toast.LENGTH_SHORT).show();

                    }
                });

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            bar =  new ProgressDialog(context);
            bar.setIndeterminate(true);
            bar.setTitle("Loading");
            bar.show();


        }
        @Override
        protected Void doInBackground(Void... params) {
            if(Utility.isOnline())
            getBooks();

            return null;
        }
    }

    public  void getBooks(){

        URL requestUrl = null;
        try {
            requestUrl = new URL("http://lastbook.altervista.org/downloadDb.php");

            URLConnection con = requestUrl.openConnection();

            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");

            }

            String result = sb.toString();
            JSONArray array= new JSONArray(result);

            books = new Book[array.length()];

            for (int i = 0; i < array.length(); i++) {

                String editore= StringEscapeUtils.unescapeHtml(array.getJSONObject(i).getString("editore"));
                String titolo=StringEscapeUtils.unescapeHtml(array.getJSONObject(i).getString("titolo"));
                String link=StringEscapeUtils.unescapeHtml(array.getJSONObject(i).getString("link"));
                String urlimg=StringEscapeUtils.unescapeHtml(array.getJSONObject(i).getString("immagine"));
                Drawable immagine = Utility.getImg(urlimg);

                Book book = new Book(editore,titolo,link,immagine,urlimg);
                books[i]=book;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}



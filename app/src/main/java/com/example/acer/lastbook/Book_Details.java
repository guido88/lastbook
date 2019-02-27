package com.example.acer.lastbook;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import org.apache.commons.lang.StringEscapeUtils;

public class Book_Details extends AppCompatActivity {

    private Detail book = null;
    private int numCom=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.mytoolbardet);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        String link = getIntent().getStringExtra("link");
        String editore = getIntent().getStringExtra("editore");
        String img = getIntent().getStringExtra("img");
        String url = "http://lastbook.altervista.org/details-" + editore + ".php";
        new GetDetails(this, url, link, img).execute();

    }


    public void getCom(String is, final Button b) {

        new AsyncTask<String, Void,String>() {
            @Override
            protected String doInBackground(String... params) {
                String toret=getNumComments(params[0]);
                return toret;
            }

            @Override
            protected void onPostExecute(String result) {

               if(result!=null){
                   b.setText("("+result+")comments");
                   numCom=Integer.parseInt(result);
               }
            }
        }.execute(is);

    }

    class GetDetails extends AsyncTask<Void, Void, Void> {

        Context context;
        private ProgressDialog bar;
        String url;
        String link;
        String img;

        public GetDetails(Context context, String url, String link, String img) {

            this.context = context;
            this.url = url;
            this.link = link;
            this.img = img;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            bar.dismiss();

            TextView v1 = (TextView) findViewById(R.id.view1);
            v1.setText(book.getTitolo());

            TextView v2 = (TextView) findViewById(R.id.view2);
            v2.setText(book.getAutore());

            TextView v3 = (TextView) findViewById(R.id.view3);
            v3.setText(book.getDescrizione());

            TextView v4 = (TextView) findViewById(R.id.view4);
            v4.setText(book.getPrezzo());

            ImageView image = (ImageView) findViewById(R.id.img);
            image.setImageDrawable(book.getImg());

            Button button = (Button) findViewById(R.id.angry_btn);

            getCom(book.getIsbn(),button);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i1 = new Intent(Book_Details.this, Rewies.class);
                    i1.putExtra("isbn", book.getIsbn());

                    if (Utility.isOnline()){
                        if(numCom>0)
                        startActivity(i1);
                        else
                            Toast.makeText(getApplicationContext(), "Nessun commento", Toast.LENGTH_SHORT).show();

                    }
                    else
                        Toast.makeText(getApplicationContext(), "Errore di connessione", Toast.LENGTH_SHORT).show();

                }
            });

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            bar = new ProgressDialog(context);
            bar.setIndeterminate(true);
            bar.setTitle("Loading");
            bar.show();


        }

        @Override
        protected Void doInBackground(Void... params) {

            if (Utility.isOnline())
                getDetails(this.url, this.link, this.img);
            else
                Toast.makeText(this.context, "Errore di connessione!", Toast.LENGTH_SHORT);

            return null;
        }
    }

    public String getNumComments(String isbn) {
        String result = "";
        try {
            String url = "http://lastbook.altervista.org/numRows.php";
            String charset = "UTF-8";
            String query = String.format("isbn=%s", URLEncoder.encode(isbn, charset));

            URLConnection connection = new URL(url).openConnection();
            connection.setDoOutput(true); // Triggers POST.
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);

            OutputStream output = connection.getOutputStream();
            output.write(query.getBytes(charset));

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            result = sb.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void getDetails(String url, String link, String u_img) {

        try {

            String charset = "UTF-8";
            String query = String.format("link=%s", URLEncoder.encode(link, charset));
            URLConnection connection = new URL(url).openConnection();
            connection.setDoOutput(true); // Triggers POST.
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
            OutputStream output = connection.getOutputStream();
            output.write(query.getBytes(charset));
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            String result = sb.toString();

            JSONObject obj = new JSONObject(result);

            Drawable immagine = Utility.getImg(u_img);
            String titolo= StringEscapeUtils.unescapeHtml(obj.getString("titolo"));
            String autore= StringEscapeUtils.unescapeHtml(obj.getString("autore"));
            String intro= StringEscapeUtils.unescapeHtml(obj.getString("intro"));
            String isbn= StringEscapeUtils.unescapeHtml(obj.getString("isbn"));
            String prezzo= StringEscapeUtils.unescapeHtml(obj.getString("prezzo"));
            book = new Detail(titolo,autore,intro,isbn, prezzo,immagine);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

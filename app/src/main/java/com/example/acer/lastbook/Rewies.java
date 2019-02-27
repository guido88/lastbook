package com.example.acer.lastbook;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class Rewies extends AppCompatActivity {

    private Review[] reviews;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewies);

        Toolbar toolbar = (Toolbar) findViewById(R.id.mytoolbarrev);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        Intent i = getIntent();
        ListView lista = (ListView) findViewById(R.id.list_r);
        new GetReviews(this,lista, i.getStringExtra("isbn")).execute();

    }


    class GetReviews extends AsyncTask<Void, Void, Void> {

        Context context;
        private ListView listview;
        private ProgressDialog bar;
        private String isb;

        public GetReviews(Context context, ListView view, String url) {
            this.listview = view;
            this.context = context;
            this.isb = url;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            bar.dismiss();
            if (reviews != null) {

                this.listview.setAdapter(new ReviewAdapter(this.context,reviews));
            }

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
                getReviews(this.isb);
            else
                Toast.makeText(getApplicationContext(), "Errore di connessione", Toast.LENGTH_SHORT).show();

            return null;
        }
    }

    public void getReviews(String isbn) {

        try {
            String url = "http://lastbook.altervista.org/reviews.php";
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
            String result = sb.toString();

            JSONArray array = new JSONArray(result);
            reviews = new Review[array.length()];

            for (int i = 0; i < array.length(); i++) {

                JSONObject temp = array.getJSONObject(i);
                String autore = StringEscapeUtils.unescapeHtml(temp.getString("autore"));
                String commento = StringEscapeUtils.unescapeHtml(temp.getString("commento"));
                reviews[i] = new Review(autore,commento);

            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

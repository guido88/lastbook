package com.example.acer.lastbook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        Button button1 = (Button) findViewById(R.id.buttonf);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i1 = new Intent(MainActivity.this, LastBook.class);
                if (Utility.isOnline())
                    startActivity(i1);
                else
                    Toast.makeText(getApplicationContext(), "Errore di connessione", Toast.LENGTH_SHORT).show();


            }

        });

        Button button2 = (Button) findViewById(R.id.data);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i2 = new Intent(MainActivity.this, MapsActivity.class);
                if (Utility.isOnline())
                    startActivity(i2);
                else
                    Toast.makeText(getApplicationContext(), "Errore di connessione", Toast.LENGTH_SHORT).show();

            }

        });

    }

}

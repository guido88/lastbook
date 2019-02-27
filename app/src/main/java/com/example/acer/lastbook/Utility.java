package com.example.acer.lastbook;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer on 08/07/2016.
 */
public class Utility {


    public static boolean isOnline() {

        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }


    public static Drawable getImg(String url) {
        Drawable result = null;

        InputStream is = null;
        try {
            is = (InputStream) new URL(url).getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        result = Drawable.createFromStream(is, "book src");
        return result;

    }

    public static Location min(Location l)  {

        List<Location> locations = new ArrayList<>();
        try {
            String url = "http://lastbook.altervista.org/coordinates.txt";

            URLConnection connection = new URL(url).openConnection();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()), 8);

            String line;
            while ((line = reader.readLine()) != null) {
                Double lat = Double.parseDouble(line.split(",")[0]);
                Double lon = Double.parseDouble(line.split(",")[1]);

                Location temp = new Location("");
                temp.setLatitude(lat);
                temp.setLongitude(lon);

                locations.add(temp);
            }
            reader.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        }
        Location min = locations.get(0);
        float distanceMin = l.distanceTo(min);

        for (int i = 1; i < locations.size(); i++) {

            float dis = l.distanceTo(locations.get(i));

            if (dis < distanceMin) {
                distanceMin = dis;
                min = locations.get(i);

            }
        }


        return min;
    }

}

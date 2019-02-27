package com.example.acer.lastbook;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {

    private GoogleMap map;
    private static final int REQUEST_RESOLVE_ERROR = 0;
    private static final String DIALOG_ERROR = null;
    private GoogleApiClient googleApiClient;
    private boolean resolvingError;
    private LocationRequest mLocationRequest;
    private boolean flag = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Toolbar toolbar = (Toolbar) findViewById(R.id.mytoolbarmap);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        map.setMyLocationEnabled(true);
        map.setMaxZoomPreference(14.0f);

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setSmallestDisplacement(5);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest, this);


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        if (flag) {
            fillMap(latitude, longitude);
            flag = false;
        }

        new GetMinLocation(this, map, location).execute(location);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        if (resolvingError)
            return;
        if (result.hasResolution()) {
            try {
                resolvingError = true;
                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                googleApiClient.connect();
            }
        } else {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this, REQUEST_RESOLVE_ERROR).show();
            resolvingError = true;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (!resolvingError) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {

        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_RESOLVE_ERROR) {
            resolvingError = false;
            if (resultCode == RESULT_OK) {
                if (!googleApiClient.isConnecting() &&
                        !googleApiClient.isConnected()) {
                    googleApiClient.connect();
                }
            }
        }
    }

    public void fillMap(double latitude, double longitude) {

        LatLng mypos = new LatLng(latitude, longitude);
        MarkerOptions options = new MarkerOptions();
        options.position(mypos);
        options.title("You are here!");
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

        map.addMarker(options);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(mypos, 9.0f));

        new AsyncTask<Double, Void, List<Place>>() {
            @Override
            protected List<Place> doInBackground(Double... xy) {
                return findNearLocation(xy[0], xy[1]);

            }

            @Override
            protected void onPostExecute(List<Place> result) {
                if (result != null) {
                    ArrayList<Place> temp = (ArrayList<Place>) result;

                    for (int i = 0; i < temp.size(); i++) {
                        Place p = temp.get(i);
                        LatLng mypos = new LatLng(p.getLatitude(), p.getLongitude());
                        MarkerOptions opt = new MarkerOptions();
                        opt.position(mypos).title(p.getName());

                        if (p.getName().contains("Mondadori"))
                            opt.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        else if (p.getName().contains("Deagostini"))
                            opt.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

                        map.addMarker(opt);

                    }
                }
            }
        }.execute(latitude, longitude);

    }

    public List<Place> findNearLocation(double latitude, double longitude) {

        PlaceService service = new PlaceService("AIzaSyDgFmVDaD5hK3sXD3nEX3WYMaR8t0JQGTY");

/* hear you should be pass the you current location latitude and langitude, */
        List<Place> findPlaces = service.findPlaces(latitude, longitude, "book_store");

        return findPlaces;
    }


    private class GetMinLocation extends AsyncTask<Location, Void, Location> {

        Context context;
        GoogleMap map;
        Location minLoc;

        public GetMinLocation(Context context, GoogleMap map, Location l) {
            this.context = context;
            this.map = map;
            this.minLoc = l;
        }

        @Override
        protected Location doInBackground(Location... params) {

            Location min = Utility.min(this.minLoc);

            return min;
        }

        @Override
        protected void onPostExecute(Location result) {

            // LatLng temp= new LatLng(result.getLatitude(),result.getLongitude());
            //map.addMarker(new MarkerOptions().position(temp).title("minimo!"));
            Log.d("LATITUDINE", result.getLatitude() + "");
            Log.d("LONG", result.getLongitude() + "");

            if (result.distanceTo(this.minLoc) < 4000f) {

                LatLng minll = new LatLng(result.getLatitude(), result.getLongitude());
                MarkerOptions options = new MarkerOptions();
                options.position(minll);
                options.title("Libro!");
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));

                this.map.addMarker(options);


                PolylineOptions polylineOptions = new PolylineOptions()
                        .add(new LatLng(minLoc.getLatitude(),minLoc.getLongitude()))
                        .add(new LatLng(result.getLatitude(),result.getLongitude()));

                Polyline polyline = this.map.addPolyline(polylineOptions);
                polyline.setColor(Color.BLUE);

                AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
                builder.setTitle("Libro trovato!");
                builder.setMessage("Sei vicino al libro,corri puoi averlo gratis!");
                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.cancel();
                    }
                });
                builder.show();

            }
        }
    }


}

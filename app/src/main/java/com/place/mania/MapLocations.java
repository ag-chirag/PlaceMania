package com.place.mania;

import android.app.Activity;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by chirag on 4/26/15.
 */
public class MapLocations extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.map_fragment,container,false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
    }
//
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        initialize();
//    }

    private void initialize()
    {
        initializeMap();
    }


    private void initializeMap()
    {
        Intent i= getActivity().getIntent();
        final String searchedaddress = i.getStringExtra("query");
        final double latitude=i.getDoubleExtra("latitude", 0);
        final double longitude=i.getDoubleExtra("longitude",0);

        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                LocationManager locationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
                Criteria criteria = new Criteria();

                Location location=new Location(searchedaddress);//locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));

                location.setLatitude(latitude);
                location.setLongitude(longitude);
                if (location != null) {
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                            .zoom(17)                   // Sets the zoom
                            .bearing(90)                // Sets the orientation of the camera to east
                            .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                            .build();                   // Creates a CameraPosition from the builder
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
                    map.addMarker(new MarkerOptions().title("Current Location").snippet("").position(current));

                }
                else
                {
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng((double)77.6886,(double)68.9908))      // Sets the center of the map to location user
                            .zoom(17)                   // Sets the zoom
                            .bearing(90)                // Sets the orientation of the camera to east
                            .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                            .build();                   // Creates a CameraPosition from the builder
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
                    map.addMarker(new MarkerOptions().title("Current Location").snippet("").position(current));

                }

            }

        });
    }

    private void getLatLong()
    {
        new Thread(){

            public void run()
            {
                String location= "hsr layout";
                String inputLine = "";
                String result = "";
                location=location.replaceAll(" ", "%20");
                String myUrl="http://maps.google.com/maps/geo?q="+location+"&output=csv";

                try{
                    URL url=new URL(myUrl);
                    URLConnection urlConnection=url.openConnection();
                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(urlConnection.getInputStream()));
                    while ((inputLine = in.readLine()) != null) {
                        result=inputLine;
                    }
                    String lat = result.substring(6, result.lastIndexOf(","));
                    String longi = result.substring(result.lastIndexOf(",") + 1);

                    Log.e("latitude",lat);
                    Log.e("longitude", longi);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }

        }.start();
    }
}

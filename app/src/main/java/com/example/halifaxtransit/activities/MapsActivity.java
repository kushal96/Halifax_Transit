package com.example.halifaxtransit.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.halifaxtransit.R;
import com.example.halifaxtransit.models.VehiclePositionDataModel;
import com.example.halifaxtransit.utils.ClusterMarker;
import com.example.halifaxtransit.utils.MyClusterManagerRenderer;
import com.example.halifaxtransit.viewmodels.MapsActivityViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import static com.example.halifaxtransit.viewmodels.MapsActivityViewModel.dataset;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double wayLat = 0.0;
    private double wayLong = 0.0;
    private MapsActivityViewModel mViewModel;
    private FloatingActionButton fabMyLocation;
    private FusedLocationProviderClient locationProviderClient;
    private static final int LOCATION_PERMISSION_REQUEST = 9001;
    private static String TAG = MapsActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        } // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //Binding the map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        fabMyLocation = findViewById(R.id.fabMyLocation);
        //Initialize the google map
        mapFragment.getMapAsync(this);
        checkLocationPermission();
        //Policy object detects and permits all thread execution policies in order to make recursive network requests on background thread
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fabMyLocation.setOnClickListener(v -> {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            // Check if the GPS is enabled or not
            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                loadCurrentLocation();
            }
            else {
                Toast.makeText(MapsActivity.this, "Enable GPS on your device first", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // This method is called when the google maps is ready to interact with.
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mViewModel = ViewModelProviders.of(this).get(MapsActivityViewModel.class);
        loadCurrentLocation();
        mViewModel.init(this);
        mViewModel.getVehicles().observe(this, vehiclePositionDataModels -> {
            Log.e(TAG, "onChanged_MAP: "+vehiclePositionDataModels.size());
            dataset = vehiclePositionDataModels;
            generateMarkers();
        });
    }

    //Method called when user allows location permission through dialog
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.
                        PERMISSION_GRANTED) {
                    loadCurrentLocation();
                }
            }
            break;
        }
    }

    private void checkLocationPermission() {
        if ((ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                &&
                (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

            //Permission not granted, ask for permissions

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
        } else {
            return;
        }
    }

    public void  loadCurrentLocation() {
        //Load the current location of user
        Log.e(TAG, "loadCurrentLocation: " );
        if (ActivityCompat.checkSelfPermission(MapsActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(MapsActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            //Permissions Granted

            Log.e(TAG, "loadCurrentLocation: Permissions Granted" );
            locationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    //Location is not empty, fetch the lattitude and longitude
                    wayLat = location.getLatitude();
                    wayLong = location.getLongitude();
                    LatLng currentLocation = new LatLng(wayLat, wayLong);
                    mMap.addMarker(new MarkerOptions().position(currentLocation).title("You're here"));
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .bearing(300)
                            .zoom(14)
                            .target(currentLocation)
                            .tilt(40)
                            .build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
                else {
                    //No location data returned
                    Log.e(TAG, "onSuccess: Location Null" );
                }
            });
        } else {
            //Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void generateMarkers()
    {
        mMap.clear();
        loadCurrentLocation();
        Log.d(TAG, "generateMarkers: ");
        if(mMap!=null) {
            for (VehiclePositionDataModel vehiclePosition : dataset) {
                Double lat = Double.valueOf(vehiclePosition.getPosition().getLatitude());
                Double longg = Double.valueOf(vehiclePosition.getPosition().getLongitude());
                Log.d(TAG, "generateMarkers: location: " + lat + " " + longg);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions
                        .title(vehiclePosition.getTrip().getRoute_id())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bus_blue))
                        .position(new LatLng(lat, longg));
                mMap.addMarker(markerOptions);
            }
        }
    }

//    }
    private void getVehicles()
    {
        mViewModel.getVehicles().observe(this, vehiclePositionDataModels -> {

        });
    }

}

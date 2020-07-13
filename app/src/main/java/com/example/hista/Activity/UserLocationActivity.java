package com.example.hista.Activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.hista.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class UserLocationActivity extends AppCompatActivity {

    private final int LOCATION_REQUEST_CODE = 1; // Request code for the location
    private FusedLocationProviderClient fusedLocationProviderClient; // make the connection to the server to find the location
    private MapView mapView;
    private GoogleMap googleMap;

    private double latitude = 0;
    private double longitude = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_location);

        // Initialize FusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Checking the location permission
        String locationPermission = Manifest.permission.ACCESS_FINE_LOCATION;

        if (ContextCompat.checkSelfPermission(this, locationPermission) == PackageManager.PERMISSION_GRANTED) {
            // Load last known location of the user
            loadLastKnownLocation();
            requestLocationUpdate();
        } else {
            // Request for permission
            requestLocationPermission();
        }

        // Reference to MapView
        mapView = findViewById(R.id.map_view);

        // Forward container's life cycle methods to mapView
        mapView.onCreate(savedInstanceState);

        // Load a map object from MapView
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                UserLocationActivity.this.googleMap = googleMap;
                zoomMapToCurrentLocation();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == LOCATION_REQUEST_CODE){
            int result = grantResults[0];
            if(result == PackageManager.PERMISSION_GRANTED){
                loadLastKnownLocation();
            } else {
                Toast.makeText(this, "Our app could not access your location.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void loadLastKnownLocation(){
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    if (task.getResult() != null) {
                        Log.d("hista-location", "onComplete: Last known location is retrieved.");
                        latitude = task.getResult().getLatitude();
                        longitude = task.getResult().getLongitude();
                        TextView txtLatitude = findViewById(R.id.txtLatitude);
                        TextView txtLongitude = findViewById(R.id.txtLongitude);
                        txtLatitude.setText("Latitude: " + latitude);
                        txtLongitude.setText("Longitude: " + longitude);
                    } else {
                        Toast.makeText(UserLocationActivity.this, "No location found.", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(UserLocationActivity.this, "Loading location failed.", Toast.LENGTH_LONG).show();
                    Log.d("hista-location", "Loading location failed: " + task.getException().getMessage());
                }
            }
        });
    }

    private void requestLocationPermission(){
        String locationPermission = Manifest.permission.ACCESS_FINE_LOCATION;
        String[] permissionsToBeRequested = new String[]{locationPermission};
        ActivityCompat.requestPermissions(this, permissionsToBeRequested, LOCATION_REQUEST_CODE);
    }

    private void requestLocationUpdate() {
        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationCallback updateLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                Log.d("hista-location", "onComplete: Last known location is updated.");

                latitude = locationResult.getLastLocation().getLatitude();
                longitude = locationResult.getLastLocation().getLongitude();
                zoomMapToCurrentLocation();

                // Remove update request
                // fusedLocationProviderClient.removeLocationUpdates(this);
            }
        };
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, updateLocationCallback, Looper.getMainLooper());
    }

    // we need other life cycle methods for map view
    @Override
    protected void onStart() {
        super.onStart();

        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mapView.onDestroy();
    }

    // Create function to zoom the map to specific location
    private void zoomMapToCurrentLocation() {
        if (latitude != 0 && longitude != 0 && googleMap != null) {
            Log.d("hista-location", "onComplete: Starting to zoom the map.");
            LatLng latLng = new LatLng(latitude, longitude);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
            googleMap.animateCamera(cameraUpdate);
        }
    }
}



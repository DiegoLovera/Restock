package com.lovera.diego.restock;

import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Objects;

public class MapOrderActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerClickListener, LocationListener {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private GoogleApiClient mGoogleApiClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private LocationRequest mLocationRequest;
    private boolean mLocationUpdateState;
    private LocationCallback mLocationCallback;
    private static final int REQUEST_CHECK_SETTINGS = 2;
    private String currentLat;
    private String currentLng;
    private String markerLat;
    private String markerLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_order);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        CardView mCardViewActualLocation = findViewById(R.id.card_actual_location);
        CardView mCardViewSelectedLocation = findViewById(R.id.card_selected_location);

        mCardViewActualLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLat != null && currentLng != null){
                    RestockApp.ACTUAL_ORDER.setLat(currentLat);
                    RestockApp.ACTUAL_ORDER.setLng(currentLng);
                    RestockApp.ACTUAL_ORDER.setStatus("1");

                    startActivity(new Intent(v.getContext(), PaymentActivity.class));
                } else {
                    Toast.makeText(v.getContext(), "Null values", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mCardViewSelectedLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (markerLng != null && markerLat != null){
                    RestockApp.ACTUAL_ORDER.setLat(markerLat);
                    RestockApp.ACTUAL_ORDER.setLng(markerLng);
                    RestockApp.ACTUAL_ORDER.setStatus("1");

                    startActivity(new Intent(v.getContext(), PaymentActivity.class));
                } else {
                    Toast.makeText(v.getContext(), "Null values", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        MapView mMapView = findViewById(R.id.mapView_order);
        if (mMapView != null){
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }

        createLocationRequest();

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    moveMap(location);
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected() && !mLocationUpdateState) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if( mGoogleApiClient != null && mGoogleApiClient.isConnected() ) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                mLocationUpdateState = true;
                startLocationUpdates();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(this);
        final double lat = 21.1566218;
        final double lng = -86.8667966;
        mMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(new LatLng(point.latitude, point.longitude)));

                markerLat = String.valueOf(point.latitude);
                markerLng = String.valueOf(point.longitude);
            }
        });

        CameraPosition camera = CameraPosition.builder()
                .target(new LatLng(lat,lng))
                .zoom(18)
                .bearing(0)
                .tilt(0)
                .build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(camera));
    }

    @Override
    public void onLocationChanged(Location location) {
        moveMap(location);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        setUpMap();
        if (mLocationUpdateState) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {android.Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        mMap.setMyLocationEnabled(true);

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        moveMap(location);
                    }
                });
    }

    protected void placeMarkerOnMap(LatLng location) {
        MarkerOptions markerOptions = new MarkerOptions().position(location);
        mMap.addMarker(markerOptions);
    }

    protected void startLocationUpdates() {
        //1
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        //2
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }

    protected void createLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                mLocationUpdateState = true;
                startLocationUpdates();
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(MapOrderActivity.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }

    private Location mLastLocation;
    private void moveMap(Location location) {
        if (location != null) {
            LatLng currentLocation = new LatLng(location.getLatitude(), location
                    .getLongitude());
            if (mLastLocation != null) {
                LatLng lastLocation = new LatLng(mLastLocation.getLatitude(),
                        mLastLocation.getLongitude());
                if (currentLocation.latitude != lastLocation.latitude
                        || currentLocation.longitude != lastLocation.longitude) {
                    //placeMarkerOnMap(currentLocation);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16));
                    currentLat = String.valueOf(location.getLatitude());
                    currentLng = String.valueOf(location.getLongitude());
                    mLastLocation = location;
                }
            } else {
                //placeMarkerOnMap(currentLocation);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16));
                currentLat = String.valueOf(location.getLatitude());
                currentLng = String.valueOf(location.getLongitude());
                mLastLocation = location;
            }
        }
    }
}

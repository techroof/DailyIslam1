package com.dailyislam;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NearbyMosqueActivity extends FragmentActivity implements
        OnMapReadyCallback, com.google.android.gms.location.LocationListener,
        GoogleApiClient.ConnectionCallbacks
        , GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<Status> {

    private GoogleMap mMap;
    GoogleApiClient client;
    LocationRequest request;
    LatLng latlng;
    double lat[], lng[];
    private static int v = 0;
    private String tag_json_obj = "json_obj_req";

    private static final String TAG = NearbyMosqueActivity.class.getSimpleName();


    private static final String NOTIFICATION_MSG = "NOTIFICATION MSG";
    // Create a Intent send by the notification
    public static Intent makeNotificationIntent(Context context, String msg) {
        Intent intent = new Intent( context, NearbyMosqueActivity.class );
        intent.putExtra( NOTIFICATION_MSG, msg );
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission
                        (this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        client = new GoogleApiClient.Builder(this).addApi(LocationServices.API)
                .addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        client.connect();

    }

    @Override
    public void onLocationChanged(Location location) {
        mMap.clear();
        if (location == null) {
            Toast.makeText(this, "location not found", Toast.LENGTH_SHORT).show();
        } else {

            latlng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latlng, 15);
            mMap.animateCamera(update);
            MarkerOptions option = new MarkerOptions();
            option.position(latlng);
            option.title("current location");
            mMap.addMarker(option);
            NearbyMosques();
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        request = new LocationRequest().create();
        request.setInterval(1000000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(client, request, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void NearbyMosques() {
        // Toast.makeText(this, "finding", Toast.LENGTH_SHORT).show();

        StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        stringBuilder.append("location=" + latlng.latitude + "," + latlng.longitude);
        stringBuilder.append("&radius=" + 1000);
        stringBuilder.append("&keyword=" + "mosques");

        stringBuilder.append("&key=" + getResources().getString(R.string.place_api));
        String url = stringBuilder.toString();
        Object dataTransfer[] = new Object[2];
        dataTransfer[0] = mMap;
        dataTransfer[1] = url;

        GetNearBY getNearBY = new GetNearBY(this);
        getNearBY.execute(dataTransfer);

        //////////////////////////---------- GEOFENCE --------------////////////////

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray resultArray = null;
                        try {
                            resultArray = response.getJSONArray("results");
                            v = resultArray.length();
                            for (int i = 0; i < resultArray.length(); i++) {
                                lat=new double[v];
                                lng=new double[v];
                                JSONObject jsonObject = resultArray.getJSONObject(i);
                                JSONObject locationObj = jsonObject.getJSONObject("geometry").getJSONObject("location");
                                lat[i] = Double.valueOf(locationObj.getString("lat"));
                                lng[i] = Double.valueOf(locationObj.getString("lng"));

                            }
                            Toast.makeText(NearbyMosqueActivity.this,
                                    "Ringer mode will be changed into Silent for 15" +
                                            "minutes when device enter 35m radius of mosque  ",
                                    Toast.LENGTH_LONG).show();
                            startGeofence();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(NearbyMosqueActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    ///////////////------------- GEOFENCE -----------///////////////////
    private Marker geoFenceMarker;

    private void markerForGeofence() {
      for (int i = 0; i < 3; i++) {
            LatLng latLng = new LatLng(lat[i], lng[i]);
            String title = latLng.latitude + ", " + latLng.longitude;
            // Define marker options
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                    .title(title);
            if (mMap != null) {
                // Remove last geoFenceMarker
                //  if (geoFenceMarker != null)
                //    geoFenceMarker.remove();

                geoFenceMarker = mMap.addMarker(markerOptions);
                //startGeofence();

            }
        }

    }

    // Start Geofence creation process
    private void startGeofence() {
        for (int i = 0; i < v; i++) {
            LatLng latLng = new LatLng(lat[i], lng[i]);

            //Log.i(TAG, "startGeofence()");
            // if( geoFenceMarker != null ) {
            Geofence geofence = createGeofence(latLng, GEOFENCE_RADIUS);

            GeofencingRequest geofenceRequest = createGeofenceRequest(geofence);
            addGeofence(geofenceRequest, i);
        }
    }

    private static final long GEO_DURATION = 60 * 60 * 1000;
    private static final String GEOFENCE_REQ_ID = "My Geofence";
    private static final float GEOFENCE_RADIUS = 35.0f; // in meters

    // Create a Geofence
    private Geofence createGeofence(LatLng latLng, float radius) {
        Log.d(TAG, "createGeofence");
        return new Geofence.Builder()
                .setRequestId(GEOFENCE_REQ_ID)
                .setCircularRegion(latLng.latitude, latLng.longitude, radius)
                .setExpirationDuration(GEO_DURATION)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER
                        | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();

    }

    // Create a Geofence Request
    private GeofencingRequest createGeofenceRequest(Geofence geofence) {
        Log.d(TAG, "createGeofenceRequest");
        return new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofence(geofence)
                .build();
    }

    private PendingIntent geoFencePendingIntent;
    private final int GEOFENCE_REQ_CODE = 0;

    private PendingIntent createGeofencePendingIntent(int i) {
        PendingIntent p;
        String j = String.valueOf(i);
        if (geoFencePendingIntent != null)
            return geoFencePendingIntent;
        Intent intent = new Intent(this, GeofenceTransitionService.class);
        intent.putExtra("value", j);
        p = PendingIntent.getService(
                this, GEOFENCE_REQ_CODE, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        return p;
    }

    // Add the created GeofenceRequest to the device's monitoring list
    private void addGeofence(GeofencingRequest request, int i) {
        Log.d(TAG, "addGeofence");
        if (checkPermission())
            LocationServices.GeofencingApi.addGeofences(
                    client,
                    request,
                    createGeofencePendingIntent(i)
            ).setResultCallback(this);
    }

    @Override
    public void onResult(@NonNull Status status) {
        Log.i(TAG, "onResult: " + status);
        if (status.isSuccess()) {
            //saveGeofence();
           // drawGeofence();
        } else {
            // inform about fail
        }
    }

    // Draw Geofence circle on GoogleMap
    private Circle geoFenceLimits;

    private void drawGeofence() {
        for (int i = 0; i < v; i++) {
            LatLng latLng1 = new LatLng(lat[i], lng[i]);
            CircleOptions circleOptions = new CircleOptions()
                    .center(latLng1)
                    .strokeColor(Color.argb(50, 70, 70, 70))
                    .fillColor(Color.argb(100, 150, 150, 150))
                    .radius(GEOFENCE_RADIUS);
            //geoFenceLimits =
            mMap.addCircle(circleOptions);
        }

        // if ( geoFenceLimits != null )
        //   geoFenceLimits.remove();


    }
    private boolean checkPermission() {
        Log.d(TAG, "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED );
    }

}
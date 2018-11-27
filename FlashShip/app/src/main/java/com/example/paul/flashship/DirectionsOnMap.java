package com.example.paul.flashship;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.paul.flashship.Class.ParseDirections;
import com.example.paul.flashship.Dowload.Download;
import com.example.paul.flashship.Dowload.DownloadData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DirectionsOnMap extends AppCompatActivity implements OnMapReadyCallback,Download,android.location.LocationListener {
    int hangid;
    Double latShipper,lngShipper;
    String sdt;
    LocationManager locationManager;
    SharedPreferences share;
    GoogleMap mMap;
    ArrayList markerPoints= new ArrayList();
    MarkerOptions options;
    Marker currentMarker,currentMarker1;
    LatLng myLatLng,vitrinhan,vitrigiao;
    List<List<HashMap<String, String>>> routes = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions_on_map);

        options = new MarkerOptions();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        hangid = bundle.getInt("hanghoaid");

        share = getSharedPreferences("sharePre",MODE_PRIVATE);

        latShipper = Double.parseDouble(share.getString("Latitude",""));
        lngShipper = Double.parseDouble(share.getString("Longitude", ""));
        sdt = share.getString("SDT","");

        myLatLng = new LatLng(latShipper,lngShipper );

        Toast.makeText(this, hangid+ "", Toast.LENGTH_SHORT).show();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapDiretions);
        mapFragment.getMapAsync(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {

        showLocation(googleMap);
        mMap = googleMap;
        String link = "http://baophuc.000webhostapp.com/flashship/loadLatLng.php?idhanghoa="+hangid;
        DownloadData downloadData = new DownloadData(this,link);
        downloadData.execute();
        try {
            String data = downloadData.get();
            Log.d("GGG",data + "aaaaaaaaaaaaaaaaaaaaa");
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("toado");
            Log.d("HHH",jsonArray.toString());
            for (int i = 0; i <jsonArray.length(); i++){
                JSONObject toado = jsonArray.optJSONObject(i);
                Double lat1 = toado.getDouble("latitude1");
                Double long1 = toado.getDouble("longitude1");
                Double lat2 = toado.getDouble("latitude2");
                Double long2 = toado.getDouble("longitude2");

                vitrinhan = new LatLng(lat1,long1);
                vitrigiao = new LatLng(lat2,long2);
                Log.d("HHH",vitrinhan.toString() + "    " + vitrigiao.toString());
                if (markerPoints.size() > 1) {
                    markerPoints.clear();
                    mMap.clear();
                }
                creatDirections();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void creatDirections() {
        markerPoints.add(vitrinhan);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.poiter_goods);


        MarkerOptions options1 = new MarkerOptions();
        options1.icon(bitmapDescriptor);
        options1.position(vitrinhan);

        markerPoints.add(vitrigiao);

        MarkerOptions options2 = new MarkerOptions();

        options2.position(vitrigiao);

        if (markerPoints.size() == 1) {
            options1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        } else if (markerPoints.size() == 2) {
            options2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(vitrinhan, 13));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(vitrinhan)
                .zoom(15)
                .bearing(90)
                .tilt(30)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        mMap.addMarker(options1);
        mMap.addMarker(options2);

        if (markerPoints.size() >= 2) {

            LatLng origin = vitrinhan;
            LatLng dest = vitrigiao;

            String url = getDirectionsUrl(origin, dest);

            DownloadData downloadTask = new DownloadData(this,url);

            downloadTask.execute(url);
            String dulieu = null;
            try {
                dulieu = downloadTask.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            Log.d("LLL",dulieu);
            ParseDirections parseDirections = new ParseDirections(dulieu);
            routes = parseDirections.parse();
            Log.d("KKK",routes.toString());
            DanDuong();
        }
    }

    private void showLocation(GoogleMap googleMap) {

        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.icon);

        options.icon(bitmapDescriptor);
        options.position(myLatLng);
        currentMarker1 = googleMap.addMarker(options);
        currentMarker1.showInfoWindow();





        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 10, (LocationListener) this);

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, 10, (LocationListener) this);
        mMap = googleMap;
    }

    private void DanDuong() {
        ArrayList points = null;
        PolylineOptions lineOptions = null;
        MarkerOptions markerOptions = new MarkerOptions();

        for (int i = 0; i < routes.size(); i++) {
            points = new ArrayList();
            lineOptions = new PolylineOptions();

            List<HashMap<String, String>> path = routes.get(i);

            for (int j = 0; j < path.size(); j++) {
                HashMap<String, String> point = path.get(j);

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                points.add(position);
            }

            lineOptions.addAll(points);
            lineOptions.width(12);
            lineOptions.color(Color.BLUE);
            lineOptions.geodesic(true);

        }
        mMap.addPolyline(lineOptions);
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        String sensor = "sensor=false";
        String mode = "mode=driving";
        String APIkey = "key=AIzaSyDC3FABtgxDyYDPdReJ7Eu8JHtGZhM0-AY";
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode + "&" + APIkey;

        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(this, "toa do thay doi", Toast.LENGTH_SHORT).show();
        String link = "http://baophuc.000webhostapp.com/flashship/updateToaDoShipper.php?latshipper=" + location.getLatitude() +"&lngshipper=" + location.getLongitude() + "&idhanghoa=" + hangid;

        DownloadData downloadData = new DownloadData(this,link);
        downloadData.execute();
        mMap.clear();
        creatDirections();
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
        options.position(latLng);
        currentMarker = mMap.addMarker(options);
        currentMarker.showInfoWindow();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public Void Download(String data) {
        return null;
    }
}

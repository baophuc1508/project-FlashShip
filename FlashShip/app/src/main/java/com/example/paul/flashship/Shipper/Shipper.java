package com.example.paul.flashship.Shipper;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paul.flashship.Adapter.AdapterHangHoa;
import com.example.paul.flashship.Class.InfoHangHoa;
import com.example.paul.flashship.Dowload.Download;
import com.example.paul.flashship.Dowload.DownloadData;
import com.example.paul.flashship.LoadHistory;
import com.example.paul.flashship.LoadInfo;
import com.example.paul.flashship.R;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Shipper extends AppCompatActivity implements OnMapReadyCallback, android.location.LocationListener, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, Download, NavigationView.OnNavigationItemSelectedListener {

    GoogleMap mMap;
    SupportMapFragment mapFragment;
    LocationManager locationManager;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    final long MIN_TIME_BW_UPDATES = 1000 * 60;
    final float MIN_DISTANCE_FOR_UPDATES = 100;
    Location myLocation, shopLocation;
    ImageView btnpowon, btnpowoff;
    boolean stop = false;
    RecyclerView recyclerShipper;
    Double lat,lng;
    SharedPreferences share;
    MarkerOptions options;
    int kiemtra = 0;
    Marker currentMarker;
    DrawerLayout drawerShipper;
    ActionBar actionBar;
    android.support.v7.widget.Toolbar toolbarShipper;
    NavigationView nav_shipper;
    String sdt,name;
    List<InfoHangHoa> listHangHoa;
    AdapterHangHoa adapterHangHoa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipper);
        options = new MarkerOptions();
        share = getSharedPreferences("sharePre",MODE_PRIVATE);

        lat = Double.parseDouble(share.getString("Latitude",""));
        lng = Double.parseDouble(share.getString("Longitude", ""));
        sdt = share.getString("SDT","");

        String link ="http://baophuc.000webhostapp.com/flashship/loadedName.php?sdt="+sdt;
        DownloadData downloadData = new DownloadData(this,link);
        downloadData.execute();

        try {
            name = downloadData.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        init();
        setDrawerLayout();

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toast.makeText(getApplicationContext(), "bạn đang offline", Toast.LENGTH_SHORT).show();
    }

    private void setDrawerLayout() {
        setSupportActionBar(toolbarShipper);
        actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");

        View hearder = nav_shipper.getHeaderView(0);

        ImageView nav_imageUser = hearder.findViewById(R.id.nav_imageUser);
        TextView nav_txtHoTen = hearder.findViewById(R.id.nav_txtHoTen);
        TextView nav_txtSdt = hearder.findViewById(R.id.nav_txtSdt);

        nav_txtHoTen.setText(name);
        nav_txtSdt.setText(sdt);
        String linkhinh = "http://baophuc.000webhostapp.com/flashship/images/"+sdt+".jpg";
        Picasso.get().load(linkhinh).into(nav_imageUser);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerShipper,R.string.open,R.string.close);
        actionBarDrawerToggle.syncState();



        toolbarShipper.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerShipper.openDrawer(GravityCompat.START);
            }
        });

        nav_shipper.setNavigationItemSelectedListener(this);


    }

    private void init() {
        btnpowoff = findViewById(R.id.btnpowoff);
        btnpowon = findViewById(R.id.btnpowon);
        toolbarShipper = findViewById(R.id.toolbarShipper);
        nav_shipper = findViewById(R.id.nav_shipper);
        drawerShipper = findViewById(R.id.drawerShipper);
        recyclerShipper = findViewById(R.id.recyclerShipper);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (stop){
            locationManager.removeUpdates(this);
        }
        else {
            Toast.makeText(this, "toa do thay doi", Toast.LENGTH_SHORT).show();
            //String link = "http://baophuc.000webhostapp.com/updatetoado.php?latitude=" + location.getLatitude() +"&longitude=" + location.getLongitude() + "&sodienthoai=" + sdt;

            //DownloadData downloadData = new DownloadData(this,link);
            //downloadData.execute();
            mMap.clear();
            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
            options.position(latLng);
            currentMarker = mMap.addMarker(options);
            currentMarker.showInfoWindow();

        }
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

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        powerClick(googleMap);
    }


    private void powerClick(final GoogleMap googleMap) {
        btnpowon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnpowoff.setVisibility(View.VISIBLE);
                btnpowon.setVisibility(View.GONE);
                if (locationManager != null) {
                    stop = true;
                    Toast.makeText(getApplicationContext(), "Bạn Đang Offline", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnpowoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnpowon.setVisibility(View.VISIBLE);
                btnpowoff.setVisibility(View.GONE);


                LatLng myLatLng = new LatLng(lat,lng );

                showMyLocation(googleMap,myLatLng);
                loadHangHoa(googleMap);

                Toast.makeText(getApplicationContext(), "Bạn Đang Online", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadHangHoa(GoogleMap googleMap) {
        String link = "http://baophuc.000webhostapp.com/flashship/loadHangHoa.php?trangthai=0";
        DownloadData downloadData = new DownloadData(this,link);
        downloadData.execute();

        Location myLocation = new Location("");
        myLocation.setLatitude(lat);
        myLocation.setLongitude(lng);

        try {
            String s = downloadData.get();
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.getJSONArray("chitiet");

            for (int i = 0;i<=jsonArray.length();i++){
                JSONObject jSonChiTiet = jsonArray.getJSONObject(i);

                Location hangHoaLocation = new Location("");
                hangHoaLocation.setLatitude(jSonChiTiet.getDouble("latitude1"));
                hangHoaLocation.setLongitude(jSonChiTiet.getDouble("longitude1"));

                double quangDuongNhan = myLocation.distanceTo(hangHoaLocation);

                listHangHoa = new ArrayList<>();
                Log.d("kiem tra qang dg", quangDuongNhan +"--------------------");
                if(quangDuongNhan < 100000){
                    Log.d("kiemtratoadotx",hangHoaLocation.getLatitude() + "------------" + hangHoaLocation.getLongitude());
                    LatLng latLng = new LatLng(hangHoaLocation.getLatitude(), hangHoaLocation.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions().position(latLng);
                    Marker marker = googleMap.addMarker(markerOptions);
                    marker.showInfoWindow();
                    int id = jSonChiTiet.getInt("idhanghoa");
                    String tenHangHoa = jSonChiTiet.getString("tenhanghoa");
                    String sdtNguoigui = jSonChiTiet.getString("sdtstore");
                    String tenNguoiNhan = jSonChiTiet.getString("tennguoinhan");
                    String sdtNguoiNhan = jSonChiTiet.getString("sdtnguoinhan");
                    double quangDuongDi = jSonChiTiet.getDouble("quangduong");
                    double chiPhi = Double.valueOf(Math.round(quangDuongDi*100)/100);

                    InfoHangHoa infoHangHoa = new InfoHangHoa(id,tenHangHoa,sdtNguoigui,tenNguoiNhan,sdtNguoiNhan,quangDuongDi,chiPhi * 5);

                    listHangHoa.add(infoHangHoa);
                }

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,1);
        recyclerShipper.setLayoutManager(layoutManager);

        adapterHangHoa = new AdapterHangHoa(this,listHangHoa,sdt);
        recyclerShipper.setAdapter(adapterHangHoa);
    }

    private void showMyLocation(GoogleMap googleMap, LatLng myLatLng) {



        if (myLatLng != null) {


            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 15));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(myLatLng)
                    .zoom(15)
                    .bearing(90)
                    .tilt(30)
                    .build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.icon);

            options.icon(bitmapDescriptor);
            options.position(myLatLng);
            currentMarker = googleMap.addMarker(options);
            currentMarker.showInfoWindow();





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
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 10, this);

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, 10, this);
            mMap = googleMap;


        }else {
            showAlert();
        }
    }


    private void showAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Shipper.this);
        alertDialog.setTitle("GPS");
        alertDialog.setMessage("GPS chưa bật. Bạn cần bật GPS.");

        alertDialog.setPositiveButton("bật GPS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Hủy Bỏ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.menuThongTin:
                Intent intent = new Intent(Shipper.this, LoadInfo.class);
                startActivity(intent);
                drawerShipper.closeDrawer(GravityCompat.START);
                return true;
            case R.id.menuLichSu:
                Intent intent1 = new Intent(Shipper.this, LoadHistory.class);
                startActivity(intent1);
                drawerShipper.closeDrawer(GravityCompat.START);
                return true;

        }
        return true;
    }
}

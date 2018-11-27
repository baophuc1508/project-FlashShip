package com.example.paul.flashship.Store;

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
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paul.flashship.Dowload.Download;
import com.example.paul.flashship.Dowload.DownloadData;
import com.example.paul.flashship.Dowload.ParseLatLng;
import com.example.paul.flashship.LoadHistory;
import com.example.paul.flashship.LoadInfo;
import com.example.paul.flashship.R;
import com.example.paul.flashship.Shipper.Shipper;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Store extends AppCompatActivity implements OnMapReadyCallback, android.location.LocationListener, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, Download, NavigationView.OnNavigationItemSelectedListener {

    GoogleMap mMap;
    SupportMapFragment mapFragment;
    LocationManager locationManager;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    final long MIN_TIME_BW_UPDATES = 1000 * 60;
    final float MIN_DISTANCE_FOR_UPDATES = 100;
    Location myLocation, shopLocation;
    boolean stop = false;
    Double lat,lng;
    SharedPreferences share;
    MarkerOptions options;
    int kiemtra = 0;
    Marker currentMarker;
    DrawerLayout drawerStore;
    ActionBar actionBar;
    android.support.v7.widget.Toolbar toolbarStore;
    NavigationView nav_store;
    String sdt,name,diachi,tenNguoiNhan,sdtNguoiNhan,tenMonHang,diaChiNguoiNhan;
    Button btnTaoDonHang,btnXacNhan;
    EditText dialog_TenMonHang,dialog_txtHoten,dialog_sdtNguoiNhan,dialog_DiaChiNguoiNhan;
    Location location,locationNguoiNhan;
    List<HashMap<String,String>> listHangHoa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        options = new MarkerOptions();
        share = getSharedPreferences("sharePre",MODE_PRIVATE);

        sdt = share.getString("SDT","");

        String link ="http://baophuc.000webhostapp.com/flashship/loadedNameDiaChi.php?sdt="+sdt;
        DownloadData downloadData = new DownloadData(this,link);
        downloadData.execute();
        String data = "";
        try {
            data = downloadData.get();
            Log.d("AAAA",data);
            JSONObject jsonObject = new JSONObject(data);
            name = jsonObject.getString("hoten");
            diachi = jsonObject.getString("diachi");

            Log.d("AAAA",name + " -- "+diachi);


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }



        init();
        setDrawerLayout();

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapStore);
        mapFragment.getMapAsync(this);

        clickTaoDonHang();
    }

    private void clickTaoDonHang() {
        btnTaoDonHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(Store.this);
                LayoutInflater inflater = LayoutInflater.from(Store.this);
                View view1 = inflater.inflate(R.layout.dialog_tao_don_hang,null);
                builder.setView(view1);

                initDonHang(view1);

                btnXacNhan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tenMonHang = dialog_TenMonHang.getText().toString();
                        tenNguoiNhan = dialog_txtHoten.getText().toString();
                        sdtNguoiNhan = dialog_sdtNguoiNhan.getText().toString();
                        diaChiNguoiNhan = dialog_DiaChiNguoiNhan.getText().toString();

                        String duongdan = "https://geocoder.api.here.com/6.2/geocode.json?searchtext="+diaChiNguoiNhan+"&app_id=UOIMI1QXlHft0KMCFGUE&app_code=t55nQfVgGWSn764usAeikQ&gen=8";
                        String link = "https://geocoder.api.here.com/6.2/geocode.json?searchtext=so%209%20duong%20nguyen%20van%20bao%20quan%20go%20vap&app_id=UOIMI1QXlHft0KMCFGUE&app_code=t55nQfVgGWSn764usAeikQ&gen=8";
                        String replaceDuongDan = duongdan.replace(" ","%20");
                        DownloadData downloadData = new DownloadData(Store.this,replaceDuongDan);
                        downloadData.execute();
                        try {
                            String data = downloadData.get();

                            ParseLatLng toadoStore = new ParseLatLng(data);
                            locationNguoiNhan = toadoStore.getLatLng();

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }

                        double s = location.distanceTo(locationNguoiNhan);

                        String quangDuong = String.valueOf(s);
                        String lat1 = String.valueOf(location.getLatitude());
                        String lng1 = String.valueOf(location.getLongitude());
                        String lat2 = String.valueOf(locationNguoiNhan.getLatitude());
                        String lng2 = String.valueOf(locationNguoiNhan.getLongitude());

                        listHangHoa = new ArrayList<>();

                        HashMap<String,String> tenhanghoa = new HashMap<>();
                        tenhanghoa.put("tenhanghoa",tenMonHang);
                        listHangHoa.add(tenhanghoa);

                        HashMap<String,String> tennguoinhan = new HashMap<>();
                        tennguoinhan.put("tennguoinhan",tenNguoiNhan);
                        listHangHoa.add(tennguoinhan);

                        HashMap<String,String> sdtnguoinhan = new HashMap<>();
                        sdtnguoinhan.put("sdtnguoinhan",sdtNguoiNhan);
                        listHangHoa.add(sdtnguoinhan);

                        HashMap<String,String> quangduong = new HashMap<>();
                        quangduong.put("quangduong",quangDuong);
                        listHangHoa.add(quangduong);

                        HashMap<String,String> sdtstore = new HashMap<>();
                        sdtstore.put("sdtstore",sdt);
                        listHangHoa.add(sdtstore);

                        HashMap<String,String> latitude1 = new HashMap<>();
                        latitude1.put("latitude1",lat1);
                        listHangHoa.add(latitude1);

                        HashMap<String,String> longitude1 = new HashMap<>();
                        longitude1.put("longitude1",lng1);
                        listHangHoa.add(longitude1);

                        HashMap<String,String> latitude2 = new HashMap<>();
                        latitude2.put("latitude2",lat2);
                        listHangHoa.add(latitude2);

                        HashMap<String,String> longitude2 = new HashMap<>();
                        longitude2.put("longitude2",lng2);
                        listHangHoa.add(longitude2);

                        HashMap<String,String> trangthai = new HashMap<>();
                        trangthai.put("trangthai","0");
                        listHangHoa.add(trangthai);

                        Log.d("EEE",listHangHoa.toString());

                        String link1 = "http://baophuc.000webhostapp.com/flashship/insertHangHoa.php";
                        DownloadData insertHangHoa = new DownloadData(Store.this,listHangHoa,link1);
                        insertHangHoa.execute();

                        String data ="";
                        int check = 5;

                        try {
                            data = insertHangHoa.get();
                            Log.d("FFF",data.toString());
                            JSONObject jsonObject = new JSONObject(data);
                            check = jsonObject.getInt("ketqua");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(check == 1){
                            Toast.makeText(getApplicationContext(),"Thêm Hàng Hóa Thành Công",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(),"Thêm Hàng Hóa Thất Bại",Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                builder.show();

            }
        });
    }

    private void initDonHang(View view1) {
        dialog_TenMonHang = view1.findViewById(R.id.dialog_TenMonHang);
        dialog_txtHoten = view1.findViewById(R.id.dialog_txtHoten);
        dialog_sdtNguoiNhan = view1.findViewById(R.id.dialog_sdtNguoiNhan);
        dialog_DiaChiNguoiNhan = view1.findViewById(R.id.dialog_DiaChiNguoiNhan);
        btnXacNhan = view1.findViewById(R.id.btnXacNhan);
    }

    private void getLocation() {
        String duongdan = "https://geocoder.api.here.com/6.2/geocode.json?searchtext="+diachi+"&app_id=UOIMI1QXlHft0KMCFGUE&app_code=t55nQfVgGWSn764usAeikQ&gen=8";
        String link = "https://geocoder.api.here.com/6.2/geocode.json?searchtext=so%209%20duong%20nguyen%20van%20bao%20quan%20go%20vap&app_id=UOIMI1QXlHft0KMCFGUE&app_code=t55nQfVgGWSn764usAeikQ&gen=8";
        Log.d("DDD",duongdan);
        String replaceDuongDan = duongdan.replace(" ","%20");
        Log.d("DDD",replaceDuongDan);
        DownloadData downloadData = new DownloadData(Store.this,replaceDuongDan);
        downloadData.execute();
        try {
            String data = downloadData.get();
            Log.d("cCC",data.toString());

            ParseLatLng toadoStore = new ParseLatLng(data);
            location = toadoStore.getLatLng();

            Log.d("BBBB",location.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void setDrawerLayout() {
        setSupportActionBar(toolbarStore);
        actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");

        View header = nav_store.getHeaderView(0);

        ImageView nav_imageUser = header.findViewById(R.id.nav_imageUser);
        TextView nav_txtHoTen = header.findViewById(R.id.nav_txtHoTen);
        TextView nav_txtSdt = header.findViewById(R.id.nav_txtSdt);

        nav_txtHoTen.setText(name);
        nav_txtSdt.setText(sdt);

        String linkhinh = "http://baophuc.000webhostapp.com/flashship/images/"+sdt+".jpg";
        Picasso.get().load(linkhinh).into(nav_imageUser);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerStore,R.string.open,R.string.close);
        actionBarDrawerToggle.syncState();

        toolbarStore.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerStore.openDrawer(GravityCompat.START);
            }
        });

        nav_store.setNavigationItemSelectedListener(this);
    }

    private void init() {
        nav_store = findViewById(R.id.nav_store);
        btnTaoDonHang = findViewById(R.id.btnTaoDonHang);
        toolbarStore = findViewById(R.id.toolbarShipper);
        drawerStore = findViewById(R.id.drawerStore);
    }

    @Override
    public void onLocationChanged(Location location) {

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

        getLocation();

        LatLng myLatLng =new LatLng(location.getLatitude(),location.getLongitude());

        if (myLatLng != null) {


            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 15));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(myLatLng)
                    .zoom(15)
                    .bearing(90)
                    .tilt(30)
                    .build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.icon_store);

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
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Store.this);
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
                Intent intent = new Intent(Store.this, LoadInfo.class);
                startActivity(intent);
                drawerStore.closeDrawer(GravityCompat.START);
                return true;
            case R.id.menuLichSu:
                Intent intent1 = new Intent(Store.this, LoadHistory.class);
                startActivity(intent1);
                drawerStore.closeDrawer(GravityCompat.START);
                return true;

        }
        return true;
    }
}

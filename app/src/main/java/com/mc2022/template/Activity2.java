package com.mc2022.template;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Activity2 extends AppCompatActivity {

    String location;
    private ListView listView;
    private WifiManager wifiManager;
    private Button b4;
    private List<ScanResult> results;
    private ArrayList<String> deviceList = new ArrayList<>();
    private ArrayList<Float> euclidean=new ArrayList<>();
    private ArrayList<String> locationList=new ArrayList<>();
    private Button b5;
    private TextView t1;
    int count;
    private int l1,l2,l3,l4,l5;

    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        b5=(Button)findViewById(R.id.b5);
        t1=(TextView) findViewById(R.id.t1);

        Intent in = getIntent();
        location = in.getStringExtra("location_user");

        listView = findViewById(R.id.wifiList);
        b4 = findViewById(R.id.button4);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        deviceList.clear();
        listView.setEmptyView(findViewById(android.R.id.empty));
        if (!wifiManager.isWifiEnabled()) {
                    Toast.makeText(getApplicationContext(), "ENABLE THE WIFI", Toast.LENGTH_LONG).show();
                    wifiManager.setWifiEnabled(true);
        }


        adapter = new ArrayAdapter<>(Activity2.this, android.R.layout.simple_list_item_1,deviceList);
        listView.setAdapter(adapter);
        getWifi();


        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KNN();
                Intent send = new Intent(Activity2.this, ResultActivity.class);
                send.putExtra("string2" , euclidean);
                send.putExtra("string3" , locationList) ;
                startActivity(send);
            }
        });

        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                if(deviceList.size() != 0) {
                    count = 0;
                    if(deviceList.size() < 10) {
                        count = 1;
                    }

                   else if(deviceList.size() < 25 && deviceList.size() > 10){
                        count = 2;
                    }

                    else if(deviceList.size() < 30 && deviceList.size() > 25){
                        count = 3;
                    }

                    else if(deviceList.size() < 40 && deviceList.size() > 30){
                        count = 4;
                    }



                    if(count == 1) {
                        t1.setText("Washroom");
                    }

                    else if(count == 2){
                        t1.setText("Lobby Girls Hostel");
                    }

                    else if(count == 3){
                        t1.setText("Staircase Girls Hostel");
                    }

                    else if(count == 4){
                        t1.setText("Girls Hostel 3rd Floor");
                    }

                    else
                        t1.setText("Sorry!!");
                }
            }
        });
    }


    BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            results = wifiManager.getScanResults();
            unregisterReceiver(this);
            deviceList.clear();
            for (ScanResult scanResult : results) {
                if (scanResult.SSID.equals("STUDENTS-N")) {
                    l1 = scanResult.level;
                }

                else if (scanResult.SSID.equals("FACULTY-STAFF-N")) {
                    l2 = scanResult.level;
                }

                else if (scanResult.SSID.equals("STUDENTS-M")) {
                    l3 = scanResult.level;
                }

                else if (scanResult.SSID.equals("SENSOR")) {
                    l4 = scanResult.level;
                }

                else if (scanResult.SSID.equals("Incubation")) {
                    l5 = scanResult.level;
                }

                deviceList.add(scanResult.SSID);
                adapter.notifyDataSetChanged();
            }
            DatabaseClass mydb = DatabaseClass.getInstance(Activity2.this);
            WifiModel wifiModel = new WifiModel(location, l1, l2, l3, l4, l5);
            mydb.getDao().insert(wifiModel);
        }
    };

    private void KNN() {
        DatabaseClass mydb = DatabaseClass.getInstance(Activity2.this);
        List<WifiModel> wifiModelList = mydb.getDao().getAllData();
        for (WifiModel wifiModel: wifiModelList) {
            float dist = euclidean(wifiModel.getR1(), wifiModel.getR2(), wifiModel.getR3(), wifiModel.getR4(), wifiModel.getR5(), l1, l2, l3, l4, l5);
            euclidean.add(dist);
            locationList.add(wifiModel.getLocation());
        }
    }

    private float euclidean(double w1, double w2, double w3, double w4,double w5, int w11, int w21, int w31, int w41, int w51) {
        float temp = (float) Math.sqrt((w11-w1)*(w11-w1) + (w21 - w2)*(w21 - w2) + (w31-w3)*(w31-w3) + (w41-w4)*(w41-w4) + (w51-w5)*(w51-w5));
        return temp;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        deviceList.clear();
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        getWifi();
    }

    private void getWifi() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(Activity2.this, "location turned off", Toast.LENGTH_SHORT).show();
                int MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 0;
                ActivityCompat.requestPermissions(Activity2.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_ACCESS_COARSE_LOCATION);
            } else {
                Toast.makeText(Activity2.this, "location turned on", Toast.LENGTH_SHORT).show();
                wifiManager.startScan();
            }
        } else {
            Toast.makeText(Activity2.this, "scanning", Toast.LENGTH_SHORT).show();
            wifiManager.startScan();
        }
    }
}
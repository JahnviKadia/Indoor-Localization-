package com.mc2022.template;

import android.content.Intent;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    private ArrayList<Float> euclidean;
    private ArrayList<String> locationList;
    private int k = 3;
    private TextView resTxt;
    Button btn3;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        resTxt = (TextView) findViewById(R.id.resTxt);
        btn3 = findViewById(R.id.btn3);

        Intent in = getIntent();
        euclidean = (ArrayList<Float>) in.getSerializableExtra("string2");
        locationList = (ArrayList<String>) in.getSerializableExtra("string3");
        String[] sortedLocation = sort(euclidean, locationList);
        String[] KNearestLoc = KNearestLoc(sortedLocation);

        resTxt.setText("Your Current Location is " + KNearestLoc[0] + ".\n" + "\nNearest Locations based on RSSI values are: " + KNearestLoc[1]);


        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                Intent i = new Intent(ResultActivity.this, PDRActivity.class);
                startActivity(i);
            }
        });

    }



    private String[] KNearestLoc(String[] sortedLocation) {
        String[] arr = new String[k];
        for (int i = 0; i < k; i++) {
            arr[i] = sortedLocation[i];
        }
        return arr;
    }

    private String[] sort(ArrayList<Float> euclidean, ArrayList<String> locationList) {
        Float[] arr1 = new Float[euclidean.size()];
        arr1 = euclidean.toArray(arr1);

        String[] arr2 = new String[locationList.size()];
        arr2 = locationList.toArray(arr2);

        for (int i=0; i<euclidean.size(); ++i)
        {
            for(int j=i+1;j<euclidean.size();j++){
                if(arr1[i]>arr1[j]){
                    float temp=arr1[j];
                    arr1[j]=arr1[i];
                    arr1[i]=temp;

                    String temp2=arr2[j];
                    arr2[j]=arr2[i];
                    arr2[i]=temp2;
                }
            }
        }
        return arr2;
    }

    public static abstract class MyLocationListener implements LocationListener {
    }
}
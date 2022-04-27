package com.mc2022.template;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main Activity";
    private SensorManager sensorManager;
    Sensor acc, mag_field;
    float[] accData = new float[3];
    float[] magData = new float[3];
    float[] orient = new float[3];
    float[] rotMat = new float[9];
    float[] I = new float[9];
    float x, y, z;
    float mag, magDelta;
    float magPre = 0;
    int stepCount = 0;
    TextView txt1, txtres;
    ImageView img1, img2;
    EditText num1, num2;
    Button btn1, btn2;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt1 = findViewById(R.id.txt1);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        txtres = findViewById(R.id.txtres);
        num1 = findViewById(R.id.num1);
        num2 = findViewById(R.id.num2);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);




        /* stride length
         females: your height * 0.413
         females: your height * 0.415
        https://www.verywellfit.com/set-pedometer-better-accuracy-3432895
        */

        // stride length
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                float n1, n2, res;
                n1 = Float.parseFloat(num1.getText().toString());
                n2 = Float.parseFloat(num2.getText().toString());
                res = n1 * n2;
                txtres.setText(Float.toString(res));
            }
        });

        // wifi scan
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                Intent i = new Intent(MainActivity.this, WifiActivity.class);
                startActivity(i);
            }
        });

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        acc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mag_field = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        // accelerometer
        SensorEventListener sensorEventListenerAcc = new SensorEventListener() {
            @Override
            public void onSensorChanged (SensorEvent sensorEvent) {
                Log.d(TAG, "Accelerometer" + sensorEvent.values[0] +" "+ sensorEvent.values[1] +" "+ sensorEvent.values[2]);
                x = sensorEvent.values[0];
                y = sensorEvent.values[1];
                z = sensorEvent.values[2];

                accData = sensorEvent.values;

                // count number of steps
                Log.i("Step1 count", String.valueOf(stepCount));


                mag = (float) Math.sqrt(x*x + y*y + z*z);
                Log.i("Magnitude", String.valueOf(mag));
                magDelta = mag - magPre;
                Log.i("MagnitudeDelta", String.valueOf(magDelta));
                magPre = mag; // reset
                Log.i("MagnitudePre", String.valueOf(magPre));

                if(magDelta == mag){
                    stepCount = 0;
                }

                else if(magDelta >= 5){
                    stepCount++;
                    Log.i("Step count", String.valueOf(stepCount));
                }

                txt1.setText(String.valueOf(stepCount));

                // direction
                boolean success = SensorManager.getRotationMatrix(rotMat, I, accData, magData);
                if(success){
                    SensorManager.getOrientation(rotMat, orient);
                    float azimuth = orient[0]; //in radians
                    azimuth = azimuth * 360 / (2 * (float) Math.PI);
                }


                img2.setRotation((float) (-orient[0]*180/3.14159));
            }

            @Override
            public void onAccuracyChanged (Sensor sensor, int i) {

            }
        };

        sensorManager.registerListener(sensorEventListenerAcc, acc, SensorManager.SENSOR_DELAY_NORMAL);

        // magnetometer
        SensorEventListener sensorEventListenerMag = new SensorEventListener() {
            @Override
            public void onSensorChanged (SensorEvent sensorEvent) {
                magData = sensorEvent.values;

                // direction
                SensorManager.getRotationMatrix(rotMat, null, accData, magData);
                SensorManager.getOrientation(rotMat, orient);

                img2.setRotation((float) (-orient[0]*180/3.14159));
            }

            @Override
            public void onAccuracyChanged (Sensor sensor, int i) {

            }
        };

        sensorManager.registerListener(sensorEventListenerMag, mag_field, SensorManager.SENSOR_DELAY_NORMAL);
    }
}


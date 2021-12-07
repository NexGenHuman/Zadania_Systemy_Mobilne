package com.example.sensorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class GravityActivity extends AppCompatActivity implements SensorEventListener {

    TextView sensorTitle;
    TextView value0;
    TextView value1;
    TextView value2;
    SensorManager sensorManager;
    Sensor gravitySensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gravity);

        sensorTitle = findViewById(R.id.gravity_title);
        value0 = findViewById(R.id.gravity_value0);
        value1 = findViewById(R.id.gravity_value1);
        value2 = findViewById(R.id.gravity_value2);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        if(gravitySensor == null) {
            sensorTitle.setText(R.string.missing_sensor);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(gravitySensor != null) {
            sensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();
        float[] currentValues = event.values;

        if(sensorType == Sensor.TYPE_GRAVITY) {
            sensorTitle.setText(R.string.gravity_sensor);
            value0.setText("X: " + currentValues[0] + "/" + gravitySensor.getMaximumRange());
            value1.setText("Y: " + currentValues[1] + "/" + gravitySensor.getMaximumRange());
            value2.setText("Z: " + currentValues[2] + "/" + gravitySensor.getMaximumRange());
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.i("check",  "onAccuaracyChanged called");
    }
}
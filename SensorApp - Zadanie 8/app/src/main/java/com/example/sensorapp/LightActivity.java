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

public class LightActivity extends AppCompatActivity implements SensorEventListener {

    TextView sensorTitle;
    TextView value;
    SensorManager sensorManager;
    Sensor lightSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);

        sensorTitle = findViewById(R.id.light_title);
        value = findViewById(R.id.light_value);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if(lightSensor == null) {
            sensorTitle.setText(R.string.missing_sensor);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(lightSensor != null) {
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
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
        float currentValue = event.values[0];

        if(sensorType == Sensor.TYPE_LIGHT) {
            sensorTitle.setText(R.string.light_sensor);
            value.setText("lx: " + currentValue + "/" + lightSensor.getMaximumRange());
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.i("check",  "onAccuaracyChanged called");
    }
}
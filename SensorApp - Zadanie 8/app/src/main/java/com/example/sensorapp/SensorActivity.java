package com.example.sensorapp;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class SensorActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private List<Sensor> sensorList;
    RecyclerView recyclerView;
    SensorAdapter adapter;

    TextView nameTextView;
    TextView typeTextView;
    LinearLayout containerLinearLayout;

    String logTag = "SensorInfo";
    public static final String subtitleVisibleKey = "subtitleVisible";
    public static final String KEY_EXTRA_SENSOR_ID = "sensorId";
    boolean subtitleVisible = false;

    public SensorActivity() {

    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_activity);

        recyclerView = findViewById((R.id.sensor_recycler_view));
        recyclerView.setLayoutManager((new LinearLayoutManager(this)));

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        for(Sensor sensor : sensorList) {
            Log.i(logTag, "Name: " + sensor.getName() + " Vendor: " + sensor.getVendor() + " Maximum range: " + sensor.getMaximumRange());
        }

        if(savedInstanceState != null) {
            subtitleVisible = savedInstanceState.getBoolean(subtitleVisibleKey);
        }

        if (adapter == null) {
            adapter = new SensorAdapter(this, sensorList);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(subtitleVisibleKey, subtitleVisible);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.sensor_menu, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (subtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_subtitle:
                subtitleVisible = !subtitleVisible;
                this.invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateView() {
        if (adapter == null) {
            adapter = new SensorAdapter(this, sensorList);
            recyclerView.setAdapter(adapter);
        } else {
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        updateSubtitle();
    }

    public void updateSubtitle() {
        int sensorsCount = sensorList.size();

        String subtitle = getString(R.string.subtitle_format, sensorsCount);
        if (!subtitleVisible) {
            subtitle = null;
        }
        AppCompatActivity appCompatActivity = (AppCompatActivity) this;
        appCompatActivity.getSupportActionBar().setSubtitle(subtitle);
    }

    private class SensorHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Sensor sensor;
        FragmentActivity activity;

        public SensorHolder(FragmentActivity activity, LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_sensor, parent, false));
            itemView.setOnClickListener(this);

            this.activity = activity;
            nameTextView = itemView.findViewById(R.id.sensor_item_name);
            typeTextView = itemView.findViewById(R.id.sensor_item_type);
            containerLinearLayout = itemView.findViewById(R.id.sensor_item_container);
        }

        public void bind(Sensor sensor) {
            this.sensor = sensor;
            nameTextView.setText(sensor.getName());
            typeTextView.setText(sensor.getStringType());

            if(sensor.getType() == Sensor.TYPE_GRAVITY || sensor.getType() == Sensor.TYPE_LIGHT) {
                containerLinearLayout.setBackgroundColor(Color.YELLOW);
            }
        }

        @Override
        public void onClick(View v) {
            if(sensor.getType() == Sensor.TYPE_GRAVITY) {
                Intent intent = new Intent(activity, GravityActivity.class);
                intent.putExtra(KEY_EXTRA_SENSOR_ID, sensor.getType());
                startActivity(intent);
            } else if(sensor.getType() == Sensor.TYPE_LIGHT) {
                Intent intent = new Intent(activity, LightActivity.class);
                intent.putExtra(KEY_EXTRA_SENSOR_ID, sensor.getType());
                startActivity(intent);
            }
        }
    }

    private class SensorAdapter extends RecyclerView.Adapter<SensorHolder> {
        private List<Sensor> sensors;
        FragmentActivity activity;

        public SensorAdapter(FragmentActivity activity, List<Sensor> sensors) {
            this.sensors = sensors;
            this.activity = activity;
        }

        @NonNull
        @Override
        public SensorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(activity);
            return new SensorHolder(activity, layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull SensorHolder holder, final int position) {
            Sensor sensor = sensors.get(position);
            holder.bind(sensor);
        }

        @Override
        public int getItemCount() {
            return sensors.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }
}
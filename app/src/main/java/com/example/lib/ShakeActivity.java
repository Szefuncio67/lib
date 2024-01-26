package com.example.lib;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ShakeActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private ShakeDetector shakeDetector;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(100);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        shakeDetector = new ShakeDetector(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake(int count) {
                progressBar.setProgress(count * 20);

                if (count >= 5) {
                    progressBar.setVisibility(View.GONE);

                    TextView bookTextView = findViewById(R.id.bookTextView);
                    bookTextView.setVisibility(View.VISIBLE);
                }
            }
        });

        sensorManager.registerListener(shakeDetector, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(shakeDetector);
    }
}
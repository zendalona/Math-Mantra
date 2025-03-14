package com.zendalona.mathmantra.utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

public class AccelerometerUtility implements SensorEventListener {

    private static final float SHAKE_THRESHOLD = 2000.0f; // Threshold for detecting shake

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private boolean isDeviceShaken = false;
    private boolean isShaking = false;
    private float lastX, lastY, lastZ;
    private long lastUpdate = 0;

    public AccelerometerUtility(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (accelerometer != null) {
                sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
            } else {
                Toast.makeText(context, "Accelerometer Sensor Unavailable", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Sensor Manager Unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    public void unregisterListener() {
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - lastUpdate) > 100) {
                long diffTime = (currentTime - lastUpdate);
                lastUpdate = currentTime;

                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                float speed = Math.abs(x + y + z - lastX - lastY - lastZ) / diffTime * 10000;
                if (speed > SHAKE_THRESHOLD && !isShaking) {
                    isDeviceShaken = true;
                    isShaking = true;
                } else if (isShaking && speed < SHAKE_THRESHOLD) {
                    isShaking = false;
                }

                lastX = x;
                lastY = y;
                lastZ = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // No need to handle accuracy changes for this use case
    }

    public boolean isDeviceShaken() {
        boolean shaken = isDeviceShaken;
        isDeviceShaken = false; // Reset the flag
        return shaken;
    }
}

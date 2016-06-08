package com.drzazga.pomiary.controller;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class CompassController implements SensorEventListener {

    public interface CompassListener {
        void compassChanged(float degree);
    }

    private final SensorManager manager;
    private Sensor sensor;
    private boolean startPosNeedInit = false;
    private float startPos;
    private CompassListener listener;

    public CompassController(Context context, CompassListener listener) {
        this.listener = listener;
        this.manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.sensor = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        manager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public boolean isAvailable() {
        return sensor != null;
    }

    public void start() {
        startPosNeedInit = true;
        manager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stop() {
        manager.unregisterListener(this);
    }

    @Override
    protected void finalize() throws Throwable {
        stop();
        super.finalize();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float val = event.values[1];
        Log.i("sensor_test", "val =  " + val);
        if (startPosNeedInit) {
            startPos = val;
            startPosNeedInit = false;
        } else
            listener.compassChanged(val - startPos);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}

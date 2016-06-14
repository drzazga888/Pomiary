package com.drzazga.pomiary.controller;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.drzazga.pomiary.utils.MathExtra;

public class DeltaCompassController implements SensorEventListener {

    private static final float TOLERANCE = (float) 0.5;

    public interface CompassListener {
        void compassChanged(float degree);
    }

    private final SensorManager manager;
    private Sensor sensor;
    private Float prevMeas, currMeas;
    private boolean established;
    private CompassListener listener;

    public DeltaCompassController(Context context, CompassListener listener) {
        this.listener = listener;
        this.manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        //noinspection deprecation
        this.sensor = manager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
    }

    public void start() {
        stop();
        prevMeas = null;
        currMeas = null;
        established = false;
        manager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stop() {
        manager.unregisterListener(this);
    }

    private void performStabilityCheck(float value) {
        prevMeas = currMeas;
        currMeas = value;
        if (prevMeas != null && Math.abs(prevMeas - currMeas) < TOLERANCE)
            established = true;
    }

    @Override
    protected void finalize() throws Throwable {
        stop();
        super.finalize();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (!established)
            performStabilityCheck(event.values[0]);
        else
            listener.compassChanged((float) MathExtra.deltaAngleDegree(currMeas, event.values[0]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}

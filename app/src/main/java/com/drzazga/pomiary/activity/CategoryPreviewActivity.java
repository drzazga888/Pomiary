package com.drzazga.pomiary.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.drzazga.pomiary.R;

import java.util.Locale;

public abstract class CategoryPreviewActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    protected EditText inputName;
    protected View rootView;
    protected SeekBar redBar, greenBar, blueBar;
    protected TextView redValue, greenValue, blueValue;
    protected RelativeLayout colorIndicator;
    protected TextView colorValue;
    protected String hexColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        redBar = (SeekBar) findViewById(R.id.redBar);
        greenBar = (SeekBar) findViewById(R.id.greenBar);
        blueBar = (SeekBar) findViewById(R.id.blueBar);
        redValue = (TextView) findViewById(R.id.redValue);
        greenValue = (TextView) findViewById(R.id.greenValue);
        blueValue = (TextView) findViewById(R.id.blueValue);
        colorIndicator = (RelativeLayout) findViewById(R.id.colorIndicator);
        colorValue = (TextView) findViewById(R.id.colorValue);
        redBar.setOnSeekBarChangeListener(this);
        greenBar.setOnSeekBarChangeListener(this);
        blueBar.setOnSeekBarChangeListener(this);

        inputName = (EditText) findViewById(R.id.categoryName);
        rootView = findViewById(android.R.id.content);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int r = redBar.getProgress();
        int g = greenBar.getProgress();
        int b = blueBar.getProgress();
        int color = Color.rgb(r, g, b);
        hexColor = String.format(Locale.getDefault(), "#%06X", 0xFFFFFF & color);
        redValue.setText(String.valueOf(r));
        greenValue.setText(String.valueOf(g));
        blueValue.setText(String.valueOf(b));
        colorIndicator.setBackgroundColor(color);
        colorValue.setText(hexColor);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}

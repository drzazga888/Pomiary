package com.drzazga.pomiary.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.drzazga.pomiary.R;
import com.drzazga.pomiary.fragment.CategorySingleFragment;

public abstract class MeasurePreviewActivity extends AppCompatActivity {

    protected EditText inputName;
    protected View rootView;
    protected CategorySingleFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_measure);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        fragment = (CategorySingleFragment) getSupportFragmentManager().findFragmentById(R.id.list_fragment);
        inputName = (EditText) findViewById(R.id.measureName);
        rootView = findViewById(android.R.id.content);
    }

}

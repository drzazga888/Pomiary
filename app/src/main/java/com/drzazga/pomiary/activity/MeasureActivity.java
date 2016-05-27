package com.drzazga.pomiary.activity;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.drzazga.pomiary.R;
import com.drzazga.pomiary.fragment.AddMeasureItemDialogFragment;
import com.drzazga.pomiary.fragment.ChangeCategoryDialogFragment;
import com.drzazga.pomiary.model.DatabaseContract;
import com.drzazga.pomiary.model.DatabaseModel;

import java.util.ArrayList;
import java.util.Collections;

public class MeasureActivity extends AppCompatActivity {

    private DatabaseModel databaseModel = new DatabaseModel(this);
    private int id;

    private void fillToolbar(Toolbar toolbar) {
        Cursor c = databaseModel.getMeasureCursor(id);
        c.moveToFirst();
        String name = c.getString(c.getColumnIndexOrThrow(DatabaseContract.ViewMeasures.COLUMN_NAME));
        String category = c.getString(c.getColumnIndexOrThrow(DatabaseContract.ViewMeasures.COLUMN_CATEGORY_NAME));
        String date = c.getString(c.getColumnIndexOrThrow(DatabaseContract.ViewMeasures.COLUMN_CREATION_DATE));
        String color = c.getString(c.getColumnIndexOrThrow(DatabaseContract.ViewMeasures.COLUMN_CATEGORY_COLOR));
        if (category == null) {
            category = getString(R.string.no_category);
            toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorNotFocused));
        }
        else
            toolbar.setBackgroundColor(Color.parseColor(color));
        toolbar.setTitle(name);
        toolbar.setSubtitle(category + ", " + date);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure);
        id = getIntent().getExtras().getInt("id");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar != null;
        fillToolbar(toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment fragment = new AddMeasureItemDialogFragment();
                fragment.show(getSupportFragmentManager(), "addMeasureItem");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_measure, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change_category:
                Bundle bundle = new Bundle();
                bundle.putIntegerArrayList("measure_ids", new ArrayList<>(Collections.singletonList(id)));
                DialogFragment fragment = new ChangeCategoryDialogFragment();
                fragment.setArguments(bundle);
                fragment.show(getSupportFragmentManager(), "change_category");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

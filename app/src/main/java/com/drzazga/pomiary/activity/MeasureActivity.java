package com.drzazga.pomiary.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.drzazga.pomiary.R;
import com.drzazga.pomiary.fragment.dialog.AddMeasureItemDialogFragment;
import com.drzazga.pomiary.fragment.dialog.ConfirmActionDialogFragment;
import com.drzazga.pomiary.fragment.dialog.ConfirmMeasureDeleteDialogFragment;
import com.drzazga.pomiary.model.DatabaseContract;
import com.drzazga.pomiary.model.MeasureProvider;

public class MeasureActivity extends AppCompatActivity implements ConfirmActionDialogFragment.OnFinishedListener {

    private ContentResolver resolver;
    private Toolbar toolbar;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure);
        id = getIntent().getExtras().getInt("id");
        resolver = getContentResolver();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

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
    protected void onResume() {
        super.onResume();
        Cursor c = resolver.query(Uri.withAppendedPath(MeasureProvider.CONTENT_URI, "measure/" + id), null, null, null, null);
        assert c != null;
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
        c.close();
        c.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_measure, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_delete:
                DialogFragment fragment = new ConfirmMeasureDeleteDialogFragment();
                fragment.setArguments(ConfirmActionDialogFragment.prepareArguments(id));
                fragment.show(getSupportFragmentManager(), "deleteMeasures");
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(this, MeasureSettingsActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFinishedDialog() {
        NavUtils.navigateUpFromSameTask(this);
    }
}

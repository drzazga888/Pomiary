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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.drzazga.pomiary.R;
import com.drzazga.pomiary.controller.GuiHacks;
import com.drzazga.pomiary.fragment.dialog.ConfirmActionDialogFragment;
import com.drzazga.pomiary.fragment.dialog.ConfirmMeasureDeleteDialogFragment;
import com.drzazga.pomiary.model.DatabaseContract;
import com.drzazga.pomiary.model.MeasureProvider;
import com.drzazga.pomiary.view.MeasureSurface;

public class MeasureActivity extends AppCompatActivity implements ConfirmActionDialogFragment.OnFinishedListener, GuiHacks {

    private ContentResolver resolver;
    private Toolbar toolbar;
    private int id;
    private RelativeLayout bar_holder;
    private MeasureSurface surface;
    private FloatingActionButton fab_add;
    private FloatingActionButton fab_modify;
    private FloatingActionButton fab_delete;
    private FloatingActionButton fab_switch;
    private LinearLayout bottomBar;

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

        fab_add = (FloatingActionButton) findViewById(R.id.fab_add);
        fab_modify = (FloatingActionButton) findViewById(R.id.fab_modify);
        fab_delete = (FloatingActionButton) findViewById(R.id.fab_delete);
        fab_switch = (FloatingActionButton) findViewById(R.id.fab_switch);
        bar_holder = (RelativeLayout) findViewById(R.id.bar_holder);
        assert bar_holder != null;
        surface = (MeasureSurface) findViewById(R.id.surface);
        assert surface != null;
        fab_add.setOnClickListener(surface.getButtonAddListener());
        fab_delete.setOnClickListener(surface.getButtonDeleteListener());
        fab_modify.setOnClickListener(surface.getButtonModifyListener());
        fab_switch.setOnClickListener(surface.getButtonSwitchListener());
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

    @Override
    public LinearLayout showBottomBar(int barRes) {
        bottomBar = (LinearLayout) getLayoutInflater().inflate(barRes, bar_holder, false);
        toolbar.setVisibility(Toolbar.INVISIBLE);
        bar_holder.addView(bottomBar);
        return bottomBar;
    }

    @Override
    public void hideBottomBar() {
        bar_holder.removeView(bottomBar);
        toolbar.setVisibility(Toolbar.VISIBLE);
    }

    @Override
    public FloatingActionButton getAddButton() {
        return fab_add;
    }

    @Override
    public FloatingActionButton getModifyButton() {
        return fab_modify;
    }

    @Override
    public FloatingActionButton getDeleteButton() {
        return fab_delete;
    }

    @Override
    public FloatingActionButton getSwitchButton() {
        return fab_switch;
    }

    @Override
    public void hideAllButtons() {
        fab_add.hide();
        fab_modify.hide();
        fab_delete.hide();
        fab_switch.hide();
    }

    @Override
    public void redraw() {
        if (surface != null)
            surface.invalidate();
    }
}

package com.drzazga.pomiary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.drzazga.pomiary.R;
import com.drzazga.pomiary.adapter.MeasureListAdapter;
import com.drzazga.pomiary.fragment.AboutDialogFragment;
import com.drzazga.pomiary.fragment.ChangeCategoryDialogFragment;
import com.drzazga.pomiary.fragment.ConfirmMeasureDeleteDialogFragment;
import com.drzazga.pomiary.model.DatabaseModel;

public class MainActivity extends AppCompatActivity {

    private DatabaseModel databaseModel = new DatabaseModel(this);
    private MeasureListAdapter measureListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), NewMeasureActivity.class);
                startActivity(intent);
            }
        });

        final ListView measureList = (ListView) findViewById(R.id.measuresView);
        assert measureList != null;
        measureList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(), MeasureActivity.class);
                intent.putExtra("id", measureListAdapter.getIdByPosition(position));
                startActivity(intent);
            }
        });
        measureList.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                measureListAdapter.setSelected(position, checked);
                mode.setTitle(getString(R.string.selected_count) + measureListAdapter.sizeSelected());
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                getMenuInflater().inflate(R.menu.menu_selected_measure, menu);
                mode.setTitle(getString(R.string.selected_count) + measureListAdapter.sizeSelected());
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                DialogFragment fragment;
                switch (item.getItemId()) {
                    case R.id.action_delete_measure:
                        fragment = new ConfirmMeasureDeleteDialogFragment();
                        fragment.setArguments(measureListAdapter.getSelectedIds());
                        fragment.show(getSupportFragmentManager(), "deleteMeasure");
                        measureListAdapter.resetSelected();
                        mode.finish();
                        return true;
                    case R.id.action_change_category:
                        fragment = new ChangeCategoryDialogFragment();
                        fragment.setArguments(measureListAdapter.getSelectedIds());
                        fragment.show(getSupportFragmentManager(), "changeCategory");
                        measureListAdapter.resetSelected();
                        mode.finish();
                        return true;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                measureListAdapter.resetSelected();
            }
        });
        measureListAdapter = new MeasureListAdapter(this, databaseModel.getMeasureCursor());
        measureList.setAdapter(measureListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                DialogFragment fragment = new AboutDialogFragment();
                fragment.show(getSupportFragmentManager(), "about");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

package com.drzazga.pomiary.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.drzazga.pomiary.R;
import com.drzazga.pomiary.adapter.CategoryListAdapter;
import com.drzazga.pomiary.model.DatabaseModel;

public class NewMeasureActivity extends AppCompatActivity {

    private DatabaseModel databaseModel = new DatabaseModel(this);
    private EditText inputName;
    private View rootView;
    CategoryListAdapter categoryListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_measure);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView lv = (ListView) findViewById(R.id.categoryListView);
        assert lv != null;
        categoryListAdapter = new CategoryListAdapter(this, databaseModel.getCategoryCursor());
        lv.setAdapter(categoryListAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                categoryListAdapter.select(position);
            }
        });

        inputName = (EditText) findViewById(R.id.measureName);
        rootView = findViewById(android.R.id.content);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_measure, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create_measure:
                try {
                    int id = (int) databaseModel.addMeasure(inputName.getText().toString(), categoryListAdapter.getSelectedId());
                    Log.i("db_test", "dodano pomiar o id = " + id);
                    Intent intent = new Intent(this, MeasureActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                } catch (SQLiteConstraintException e) {
                    Snackbar.make(rootView, R.string.measure_name_constraint, Snackbar.LENGTH_LONG).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

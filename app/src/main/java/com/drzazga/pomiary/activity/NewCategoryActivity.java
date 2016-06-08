package com.drzazga.pomiary.activity;

import android.content.ContentValues;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.drzazga.pomiary.R;
import com.drzazga.pomiary.model.DatabaseContract;
import com.drzazga.pomiary.model.MeasureProvider;

public class NewCategoryActivity extends CategoryPreviewActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onProgressChanged(null, 0, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_only_acc, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_accept:
                try {
                    ContentValues values = new ContentValues();
                    values.put(DatabaseContract.Categories.COLUMN_NAME, inputName.getText().toString());
                    values.put(DatabaseContract.Categories.COLUMN_COLOR, hexColor);
                    getContentResolver().insert(Uri.withAppendedPath(MeasureProvider.CONTENT_URI, "category"), values);
                    NavUtils.navigateUpFromSameTask(this);
                } catch (IllegalArgumentException e) {
                    Snackbar.make(rootView, R.string.name_not_empty_contraint, Snackbar.LENGTH_LONG).show();
                } catch (SQLException e) {
                    Snackbar.make(rootView, R.string.name_unique_contraint, Snackbar.LENGTH_LONG).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

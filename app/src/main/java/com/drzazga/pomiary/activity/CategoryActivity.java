package com.drzazga.pomiary.activity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.drzazga.pomiary.R;
import com.drzazga.pomiary.fragment.dialog.ConfirmActionDialogFragment;
import com.drzazga.pomiary.fragment.dialog.ConfirmCategoryDeleteDialogFragment;
import com.drzazga.pomiary.fragment.dialog.ConfirmMeasureDeleteDialogFragment;
import com.drzazga.pomiary.model.DatabaseContract;
import com.drzazga.pomiary.model.MeasureProvider;

public class CategoryActivity extends CategoryPreviewActivity implements ConfirmActionDialogFragment.OnFinishedListener {

    private int id;
    private ContentResolver resolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getIntent().getExtras().getInt("id");
        resolver = getContentResolver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Cursor c = resolver.query(Uri.withAppendedPath(MeasureProvider.CONTENT_URI, "category/" + id), null, null, null, null);
        assert c != null;
        c.moveToFirst();
        int color = Color.parseColor(c.getString(c.getColumnIndexOrThrow(DatabaseContract.Categories.COLUMN_COLOR)));
        inputName.setText(c.getString(c.getColumnIndexOrThrow(DatabaseContract.Categories.COLUMN_NAME)));
        c.close();
        redBar.setProgress((color >> 16) & 0xFF);
        greenBar.setProgress((color >> 8) & 0xFF);
        blueBar.setProgress((color) & 0xFF);
        onProgressChanged(null, 0, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_category, menu);
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
                    resolver.update(Uri.withAppendedPath(MeasureProvider.CONTENT_URI, "category/" + id), values, null, null);
                    NavUtils.navigateUpFromSameTask(this);
                } catch (IllegalArgumentException e) {
                    Snackbar.make(rootView, R.string.name_not_empty_contraint, Snackbar.LENGTH_LONG).show();
                } catch (SQLException e) {
                    Snackbar.make(rootView, R.string.name_unique_contraint, Snackbar.LENGTH_LONG).show();
                }
                return true;
            case R.id.action_delete:
                DialogFragment fragment = new ConfirmCategoryDeleteDialogFragment();
                fragment.setArguments(ConfirmActionDialogFragment.prepareArguments(id));
                fragment.show(getSupportFragmentManager(), "deleteMeasures");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFinishedDialog() {
        NavUtils.navigateUpFromSameTask(this);
    }
}

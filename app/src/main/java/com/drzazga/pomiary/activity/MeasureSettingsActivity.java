package com.drzazga.pomiary.activity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
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

public class MeasureSettingsActivity extends MeasurePreviewActivity {

    private int id;
    private ContentResolver resolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        id = getIntent().getExtras().getInt("id");
        super.onCreate(savedInstanceState);
        resolver = getContentResolver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Cursor c = resolver.query(Uri.withAppendedPath(MeasureProvider.CONTENT_URI, "measure/" + id), null, null, null, null);
        assert c != null;
        c.moveToFirst();
        inputName.setText(c.getString(c.getColumnIndexOrThrow(DatabaseContract.ViewMeasures.COLUMN_NAME)));
        int categoryIndex = c.getColumnIndexOrThrow(DatabaseContract.ViewMeasures.COLUMN_CATEGORY_ID);
        Integer categoryId = c.isNull(categoryIndex) ? null : c.getInt(categoryIndex);
        fragment.getAdapter().selectById(categoryId);
        c.close();
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
                    Integer selected = fragment.getAdapter().getSelectedMaxOne();
                    values.put(DatabaseContract.Measures.COLUMN_NAME, inputName.getText().toString());
                    values.put(DatabaseContract.Measures.COLUMN_CATEGORY_ID,
                            (Integer) (selected == null ? null : fragment.getLayoutManager().findViewByPosition(selected).getTag())
                    );
                    resolver.update(Uri.withAppendedPath(MeasureProvider.CONTENT_URI, "measure/" + id), values, null, null);
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

package com.drzazga.pomiary.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.SQLException;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.drzazga.pomiary.R;
import com.drzazga.pomiary.model.DatabaseContract;
import com.drzazga.pomiary.model.MeasureProvider;

public class NewMeasureActivity extends MeasurePreviewActivity {

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
                    Integer categoryId = fragment.getAdapter().getSelectedMaxOne();
                    if (categoryId != null)
                        categoryId = (Integer) fragment.getLayoutManager().findViewByPosition(categoryId).getTag();
                    ContentValues values = new ContentValues();
                    values.put(DatabaseContract.Measures.COLUMN_NAME, inputName.getText().toString());
                    values.put(DatabaseContract.Measures.COLUMN_CATEGORY_ID, categoryId);
                    Uri measureUri = getContentResolver().insert(Uri.withAppendedPath(MeasureProvider.CONTENT_URI, "measure"), values);
                    assert measureUri != null;
                    Intent intent = new Intent(this, MeasureActivity.class);
                    intent.putExtra("id", Integer.valueOf(measureUri.getPathSegments().get(1)));
                    startActivity(intent);
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

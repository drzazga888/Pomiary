package com.drzazga.pomiary.activity;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.drzazga.pomiary.R;
import com.drzazga.pomiary.adapter.HomePagerAdapter;
import com.drzazga.pomiary.fragment.CategorySingleFragment;
import com.drzazga.pomiary.model.MeasureProvider;

public class MeasurePreviewActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, OnDemandLoaderRestarter {

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

    @Override
    protected void onResume() {
        super.onResume();
        restartLoader(null);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, Uri.withAppendedPath(MeasureProvider.CONTENT_URI, "category"), null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i("testing", this + " -> LoaderManagerCallback -> onLoadFinished, id = " + loader.getId());
        fragment.getAdapter().swapCursor(data);
        fragment.toggleListVisibilityIfNeeded(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        fragment.getAdapter().swapCursor(null);
    }

    @Override
    public void restartLoader(Integer id) {
        getLoaderManager().restartLoader(HomePagerAdapter.CATEGORY_LIST_FRAGMENT_ID, null, this);
    }
}

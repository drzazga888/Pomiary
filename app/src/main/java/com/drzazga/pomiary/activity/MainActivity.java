package com.drzazga.pomiary.activity;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.drzazga.pomiary.R;
import com.drzazga.pomiary.adapter.HomePagerAdapter;
import com.drzazga.pomiary.fragment.MultiChoiceListFragment;
import com.drzazga.pomiary.fragment.dialog.AboutDialogFragment;
import com.drzazga.pomiary.model.MeasureProvider;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor>, OnDemandLoaderRestarter {

    private ViewPager pager;
    private HomePagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new HomePagerAdapter(getSupportFragmentManager(), this);
        pager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        assert tabLayout != null;
        tabLayout.setupWithViewPager(pager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        restartLoader(HomePagerAdapter.MEASURE_LIST_FRAGMENT_ID);
        restartLoader(HomePagerAdapter.CATEGORY_LIST_FRAGMENT_ID);
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

    @Override
    public void onClick(View v) {
        Class<?> target;
        switch (pager.getCurrentItem()) {
            case 0:
                target = NewMeasureActivity.class;
                break;
            case 1:
                target = NewCategoryActivity.class;
                break;
            default:
                throw new RuntimeException();
        }
        Intent intent = new Intent(this, target);
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case HomePagerAdapter.MEASURE_LIST_FRAGMENT_ID:
                return new CursorLoader(this, Uri.withAppendedPath(MeasureProvider.CONTENT_URI, "measure"), null, null, null, null);
            case HomePagerAdapter.CATEGORY_LIST_FRAGMENT_ID:
                return new CursorLoader(this, Uri.withAppendedPath(MeasureProvider.CONTENT_URI, "category"), null, null, null, null);
        }
        return null;
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i("testing", this + " -> LoaderManagerCallback -> onLoadFinished, id = " + loader.getId());
        MultiChoiceListFragment fragment = pagerAdapter.getSpecificItem(loader.getId());
        fragment.getAdapter().swapCursor(data);
        fragment.toggleListVisibilityIfNeeded(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        pagerAdapter.getSpecificItem(loader.getId()).getAdapter().swapCursor(null);
    }

    @Override
    public void restartLoader(Integer id) {
        getLoaderManager().restartLoader(id, null, this);
    }
}

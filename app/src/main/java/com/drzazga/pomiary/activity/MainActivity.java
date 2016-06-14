package com.drzazga.pomiary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.drzazga.pomiary.R;
import com.drzazga.pomiary.adapter.HomePagerAdapter;
import com.drzazga.pomiary.fragment.dialog.AboutDialogFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pager = (ViewPager) findViewById(R.id.pager);
        HomePagerAdapter pagerAdapter = new HomePagerAdapter(getSupportFragmentManager(), this);
        pager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        assert tabLayout != null;
        tabLayout.setupWithViewPager(pager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(this);
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
}

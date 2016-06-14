package com.drzazga.pomiary.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.ActionMode;
import android.view.MenuItem;
import android.view.View;

import com.drzazga.pomiary.R;
import com.drzazga.pomiary.activity.MeasureActivity;
import com.drzazga.pomiary.adapter.HomePagerAdapter;
import com.drzazga.pomiary.adapter.MeasureListAdapter;
import com.drzazga.pomiary.adapter.MultiChoiceCursorAdapter;
import com.drzazga.pomiary.fragment.dialog.ConfirmActionDialogFragment;
import com.drzazga.pomiary.fragment.dialog.ConfirmMeasureDeleteDialogFragment;
import com.drzazga.pomiary.model.MeasureProvider;

public class MeasureListFragment extends MultiChoiceListFragment {

    @Override
    protected int getLoaderId() {
        return HomePagerAdapter.MEASURE_LIST_ID;
    }

    @Override
    protected MultiChoiceCursorAdapter instantiateAdapter() {
        return new MeasureListAdapter(getContext());
    }

    @Override
    @MenuRes
    protected int getContextMenuRes() {
        return R.menu.menu_only_delete;
    }

    @Override
    @StringRes
    protected int getEmptyListMessage() {
        return R.string.measure_list_empty;
    }

    @Override
    public void onMultiListItemClicked(View v, int position) {
        Intent intent = new Intent(getActivity(), MeasureActivity.class);
        intent.putExtra("id", (Integer) layoutManager.findViewByPosition(position).getTag());
        startActivity(intent);
    }

    @Override
    public boolean onMultiActionItemClicked(ActionMode mode, MenuItem item) {
        DialogFragment fragment;
        switch (item.getItemId()) {
            case R.id.action_delete:
                fragment = new ConfirmMeasureDeleteDialogFragment();
                fragment.setArguments(ConfirmActionDialogFragment.prepareArguments(adapter, layoutManager));
                fragment.setTargetFragment(this, 0);
                fragment.show(getFragmentManager(), "deleteMeasures");
                mode.finish();
                return true;
        }
        return false;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(), Uri.withAppendedPath(MeasureProvider.CONTENT_URI, "measure"), null, null, null, null);
    }
}

package com.drzazga.pomiary.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.ActionMode;
import android.view.MenuItem;
import android.view.View;

import com.drzazga.pomiary.R;
import com.drzazga.pomiary.activity.CategoryActivity;
import com.drzazga.pomiary.adapter.CategoryListAdapter;
import com.drzazga.pomiary.adapter.HomePagerAdapter;
import com.drzazga.pomiary.adapter.MultiChoiceCursorAdapter;
import com.drzazga.pomiary.fragment.dialog.ConfirmActionDialogFragment;
import com.drzazga.pomiary.fragment.dialog.ConfirmCategoryDeleteDialogFragment;
import com.drzazga.pomiary.model.MeasureProvider;

public class CategoryListFragment extends MultiChoiceListFragment {

    @Override
    protected int getLoaderId() {
        return HomePagerAdapter.CATEGORY_LIST_ID;
    }

    @Override
    protected MultiChoiceCursorAdapter instantiateAdapter() {
        return new CategoryListAdapter(getContext());
    }

    @Override
    protected int getContextMenuRes() {
        return R.menu.menu_only_delete;
    }

    @Override
    protected int getEmptyListMessage() {
        return R.string.category_list_empty;
    }

    @Override
    public void onMultiListItemClicked(View v, int position) {
        Intent intent = new Intent(getActivity(), CategoryActivity.class);
        intent.putExtra("id", (Integer) layoutManager.findViewByPosition(position).getTag());
        startActivity(intent);
    }

    @Override
    public boolean onMultiActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                DialogFragment fragment = new ConfirmCategoryDeleteDialogFragment();
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
        return new CursorLoader(getContext(), Uri.withAppendedPath(MeasureProvider.CONTENT_URI, "category"), null, null, null, null);
    }
}

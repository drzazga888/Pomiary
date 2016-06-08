package com.drzazga.pomiary.fragment;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.util.Log;
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

public class CategoryListFragment extends MultiChoiceListFragment {

    @Override
    protected MultiChoiceCursorAdapter instantiateAdapter() {
        return new CategoryListAdapter(getContext(), null);
    }

    @Override
    protected int getMenuRes() {
        return R.menu.menu_only_delete;
    }

    @Override
    protected int getEmptyListMessage() {
        return R.string.category_list_empty;
    }

    @Override
    protected void reloadLoaders() {
        Log.i("testing", "restartuje loadery z kategory fragment");
        loaderRestarter.restartLoader(HomePagerAdapter.MEASURE_LIST_FRAGMENT_ID);
        loaderRestarter.restartLoader(HomePagerAdapter.CATEGORY_LIST_FRAGMENT_ID);
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
}

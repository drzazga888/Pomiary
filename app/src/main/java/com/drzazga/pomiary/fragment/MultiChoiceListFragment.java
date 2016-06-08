package com.drzazga.pomiary.fragment;

import android.support.annotation.MenuRes;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.drzazga.pomiary.R;

public abstract class MultiChoiceListFragment extends BaseListFragment
        implements MultiChoiceListCallbacks {

    @MenuRes private int menuRes;
    private ActionMode savedMode = null;

    private String getTitle() {
        return getString(R.string.selected_count) + adapter.selectedCount();
    }

    public MultiChoiceListFragment() {
        super();
        menuRes = getMenuRes();
    }

    @MenuRes
    protected abstract int getMenuRes();

    @Override
    public void onClick(View v, int position) {
        if (savedMode != null) {
            adapter.toggleSelection(position);
            savedMode.setTitle(getTitle());
            if (adapter.selectedCount() == 0) {
                savedMode.finish();
                savedMode = null;
            }
        } else
            onMultiListItemClicked(v, position);
    }

    @Override
    public boolean onLongClick(View v, int position) {
        if (savedMode == null) {
            adapter.toggleSelection(position);
            getActivity().startActionMode(new ActionMode.Callback() {

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    savedMode = mode;
                    mode.setTitle(getTitle());
                    getActivity().getMenuInflater().inflate(menuRes, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    return onMultiActionItemClicked(mode, item);
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {
                    savedMode = null;
                    adapter.clearSelection();
                }
            });
            return true;
        }
        return false;
    }
}
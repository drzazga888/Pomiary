package com.drzazga.pomiary.fragment;

import android.view.ActionMode;
import android.view.MenuItem;
import android.view.View;

public interface MultiChoiceListCallbacks {
    void onMultiListItemClicked(View v, int position);
    boolean onMultiActionItemClicked(ActionMode mode, MenuItem item);
}

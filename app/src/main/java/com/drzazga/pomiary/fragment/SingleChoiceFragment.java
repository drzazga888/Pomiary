package com.drzazga.pomiary.fragment;

import android.view.View;

public abstract class SingleChoiceFragment extends BaseListFragment {

    public SingleChoiceFragment() {
        super();
    }

    @Override
    public void onClick(View v, int position) {
        adapter.toggleSelectionMaxOne(position);
    }

    @Override
    public boolean onLongClick(View v, int position) {
        return false;
    }
}

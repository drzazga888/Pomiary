package com.drzazga.pomiary.fragment;

import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.drzazga.pomiary.R;
import com.drzazga.pomiary.adapter.CategoryListAdapter;
import com.drzazga.pomiary.adapter.HomePagerAdapter;
import com.drzazga.pomiary.adapter.MultiChoiceCursorAdapter;
import com.drzazga.pomiary.model.MeasureProvider;

public class CategorySingleFragment extends SingleChoiceFragment {

    @Override
    protected int getLoaderId() {
        return HomePagerAdapter.CATEGORY_LIST_ID;
    }

    @Override
    protected MultiChoiceCursorAdapter instantiateAdapter() {
        return new CategoryListAdapter(getActivity());
    }

    @Override
    protected int getEmptyListMessage() {
        return R.string.category_list_empty;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(), Uri.withAppendedPath(MeasureProvider.CONTENT_URI, "category"), null, null, null, null);
    }
}

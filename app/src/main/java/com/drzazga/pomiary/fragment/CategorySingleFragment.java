package com.drzazga.pomiary.fragment;

import com.drzazga.pomiary.R;
import com.drzazga.pomiary.adapter.CategoryListAdapter;
import com.drzazga.pomiary.adapter.MultiChoiceCursorAdapter;

public class CategorySingleFragment extends SingleChoiceFragment {

    @Override
    protected MultiChoiceCursorAdapter instantiateAdapter() {
        return new CategoryListAdapter(getActivity(), null);
    }

    @Override
    protected int getEmptyListMessage() {
        return R.string.category_list_empty;
    }

    @Override
    protected void reloadLoaders() {
        loaderRestarter.restartLoader(null);
    }
}

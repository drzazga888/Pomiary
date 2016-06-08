package com.drzazga.pomiary.fragment.dialog;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.drzazga.pomiary.R;
import com.drzazga.pomiary.model.MeasureProvider;

public class ConfirmCategoryDeleteDialogFragment extends ConfirmActionDialogFragment {

    private ContentResolver resolver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resolver = getActivity().getContentResolver();
    }

    @Override
    protected void onActionForSingleElement(int id) {
        resolver.delete(Uri.withAppendedPath(MeasureProvider.CONTENT_URI, "category/" + id), null, null);
    }

    @Override
    protected int getPositiveTextButton() {
        return R.string.delete;
    }

    @Override
    @StringRes
    protected int getMessage() {
        return R.string.delete_category_message;
    }

}

package com.drzazga.pomiary.fragment.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;

import com.drzazga.pomiary.R;
import com.drzazga.pomiary.adapter.MultiChoiceCursorAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class ConfirmActionDialogFragment extends DialogFragment implements View.OnClickListener {

    @Override
    public void onClick(View v) {

    }

    public interface OnFinishedListener {
        void onFinishedDialog();
    }

    private OnFinishedListener callback;

    @Override
    public void onStart() {
        super.onStart();
        final AlertDialog d = (AlertDialog) getDialog();
        Button positiveButton = d.getButton(Dialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!beforeActionPerformed())
                    return;
                List<Integer> ids = getArguments().getIntegerArrayList("ids");
                if (ids != null) {
                    for (Integer id : ids)
                        onActionForSingleElement(id);
                }
                if (callback != null)
                    callback.onFinishedDialog();
                d.dismiss();
            }
        });
    }

    protected boolean beforeActionPerformed() {
        return true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            if (getTargetFragment() != null)
                callback = (OnFinishedListener) getTargetFragment();
            else
                callback = (OnFinishedListener) getActivity();
        } catch (ClassCastException e) {
            callback = null;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getMessage())
                .setPositiveButton(getPositiveTextButton(), null)
                .setNegativeButton(R.string.cancel, null);
        return bindToBuilder(builder).create();
    }

    protected AlertDialog.Builder bindToBuilder(AlertDialog.Builder builder) {
        return builder;
    }

    protected abstract void onActionForSingleElement(int id);

    @StringRes
    protected abstract int getPositiveTextButton();

    @StringRes
    protected abstract int getMessage();

    public static Bundle prepareArguments(MultiChoiceCursorAdapter adapter, RecyclerView.LayoutManager layoutManager) {
        Bundle bundle = new Bundle();
        ArrayList<Integer> list = new ArrayList<>();
        SparseBooleanArray selected = adapter.getSelected();
        Integer singleSelected = adapter.getSelectedMaxOne();
        for (int i = 0; i < selected.size(); ++i) {
            list.add((Integer) layoutManager.findViewByPosition(selected.keyAt(i)).getTag());
            adapter.notifyItemRemoved(selected.keyAt(i));
        }
        if (singleSelected != null)
            list.add((Integer) layoutManager.findViewByPosition(singleSelected).getTag());
        bundle.putIntegerArrayList("ids", list);
        return bundle;
    }

    public static Bundle prepareArguments(int id) {
        Bundle bundle = new Bundle();
        ArrayList<Integer> list = new ArrayList<>(1);
        list.add(id);
        bundle.putIntegerArrayList("ids", list);
        return bundle;
    }
}

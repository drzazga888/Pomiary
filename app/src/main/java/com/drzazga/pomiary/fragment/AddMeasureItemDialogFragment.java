package com.drzazga.pomiary.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.drzazga.pomiary.R;

public class AddMeasureItemDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.AddMeasureItem))
                .setItems(R.array.measure_item_types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("dialog", "AddMeasureItemDialogFragment.onClick(" + which + ")");
                    }
                })
                .setNegativeButton(R.string.cancel, null);
        return builder.create();
    }
}

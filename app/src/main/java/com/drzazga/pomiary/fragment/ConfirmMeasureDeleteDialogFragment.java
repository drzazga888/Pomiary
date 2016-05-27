package com.drzazga.pomiary.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.drzazga.pomiary.R;
import com.drzazga.pomiary.model.DatabaseModel;

import java.util.ArrayList;

public class ConfirmMeasureDeleteDialogFragment extends DialogFragment {

    private DatabaseModel databaseModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseModel = new DatabaseModel(getContext());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.delete_measure_message))
                .setPositiveButton(R.string.remove, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("dialog_test", "usuwanie pomiaru, args = " + getArguments());
                        ArrayList<Integer> measureIds = getArguments().getIntegerArrayList("measure_ids");
                        assert measureIds != null;
                        for (int measureId : measureIds)
                            databaseModel.removeMeasure(measureId);
                    }
                })
                .setNegativeButton(R.string.cancel, null);
        return builder.create();
    }
}

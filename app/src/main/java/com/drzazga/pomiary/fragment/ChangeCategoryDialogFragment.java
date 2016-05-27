package com.drzazga.pomiary.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.drzazga.pomiary.R;
import com.drzazga.pomiary.adapter.CategoryListAdapter;
import com.drzazga.pomiary.model.DatabaseModel;

import java.util.ArrayList;

public class ChangeCategoryDialogFragment extends DialogFragment {

    private DatabaseModel databaseModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseModel = new DatabaseModel(getContext());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final CategoryListAdapter categoryListAdapter = new CategoryListAdapter(getContext(), databaseModel.getCategoryCursor());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.change_category)
                .setAdapter(categoryListAdapter, null)
                .setPositiveButton(getString(R.string.change), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("dialog_test", "zmiana kategorii, which = " + which + ", args = " + getArguments());
                        Integer categoryId = categoryListAdapter.getSelectedId();
                        ArrayList<Integer> measureIds = getArguments().getIntegerArrayList("measure_ids");
                        assert measureIds != null;
                        for (int measureId : measureIds)
                            databaseModel.changeCategory(measureId, categoryId);
                    }
                })
                .setNegativeButton(R.string.cancel, null);
        AlertDialog dialog = builder.create();
        ListView lv = dialog.getListView();
        lv.setItemsCanFocus(false);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("bbb", String.valueOf(position));
                categoryListAdapter.select(position);
            }
        });
        return dialog;
    }

}

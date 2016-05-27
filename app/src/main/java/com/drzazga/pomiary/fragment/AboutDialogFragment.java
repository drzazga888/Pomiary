package com.drzazga.pomiary.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;

import com.drzazga.pomiary.R;

/**
 * Created by mario on 27.05.16.
 */
public class AboutDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ImageView image = new ImageView(getActivity());
        image.setImageResource(R.mipmap.ic_launcher);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.app_name)
                .setIcon(R.mipmap.ic_launcher)
                .setMessage(R.string.app_description)
                .setNeutralButton(R.string.close, null);
        return builder.create();
    }
}

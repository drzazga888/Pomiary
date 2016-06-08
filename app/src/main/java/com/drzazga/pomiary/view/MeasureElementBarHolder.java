package com.drzazga.pomiary.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.drzazga.pomiary.R;

public class MeasureElementBarHolder extends RecyclerView.ViewHolder {

    public ImageButton back, accept;
    public EditText name;

    public MeasureElementBarHolder(View itemView) {
        super(itemView);
        this.back = (ImageButton) itemView.findViewById(R.id.action_back);
        this.name = (EditText) itemView.findViewById(R.id.input_name);
        this.accept = (ImageButton) itemView.findViewById(R.id.action_accept);
    }
}

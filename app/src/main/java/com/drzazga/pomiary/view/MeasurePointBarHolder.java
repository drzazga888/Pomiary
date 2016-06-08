package com.drzazga.pomiary.view;

import android.view.View;
import android.widget.EditText;

import com.drzazga.pomiary.R;

public class MeasurePointBarHolder extends MeasureElementBarHolder {

    public EditText x, y;

    public MeasurePointBarHolder(View itemView) {
        super(itemView);
        x = (EditText) itemView.findViewById(R.id.input_x);
        y = (EditText) itemView.findViewById(R.id.input_y);
    }
}

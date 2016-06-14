package com.drzazga.pomiary.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.drzazga.pomiary.R;
import com.drzazga.pomiary.model.DatabaseContract;

public class MeasureListAdapter extends MultiChoiceCursorAdapter<MeasureListAdapter.MeasureViewHolder> {

    public static class MeasureViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView date;
        public TextView category;
        public LinearLayout border;

        public MeasureViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.measureName);
            date = (TextView) itemView.findViewById(R.id.measureDate);
            category = (TextView) itemView.findViewById(R.id.measureCategory);
            border = (LinearLayout) itemView.findViewById(R.id.measureBorder);
        }
    }

    public MeasureListAdapter(Context context) {
        super(context);
    }

    @Override
    public MeasureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.measure_list_item, parent, false);
        return new MeasureViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MeasureViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ViewMeasures.COLUMN_NAME));
        String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ViewMeasures.COLUMN_CREATION_DATE));
        String category = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ViewMeasures.COLUMN_CATEGORY_NAME));
        String borderColor = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ViewMeasures.COLUMN_CATEGORY_COLOR));
        holder.name.setText(name);
        holder.date.setText(date);
        if (category != null)
            holder.category.setText(category);
        else
            holder.category.setText(context.getString(R.string.no_category));
        if (borderColor != null)
            holder.border.setBackgroundColor(Color.parseColor(borderColor));
        else
            holder.border.setBackgroundColor(ContextCompat.getColor(context, R.color.colorNotFocused));
    }
}

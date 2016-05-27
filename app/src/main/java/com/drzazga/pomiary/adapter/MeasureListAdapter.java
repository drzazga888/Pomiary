package com.drzazga.pomiary.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.drzazga.pomiary.R;
import com.drzazga.pomiary.model.DatabaseContract;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MeasureListAdapter extends CursorAdapter  {

    private static Set<Integer> selectedSet = new HashSet<>();

    public MeasureListAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    public void setSelected(int position, boolean selected) {
        if (selected)
            selectedSet.add(position);
        else
            selectedSet.remove(position);
        notifyDataSetChanged();
    }

    public int sizeSelected() {
        return selectedSet.size();
    }

    public void resetSelected() {
        selectedSet.clear();
        notifyDataSetChanged();
    }

    public Bundle getSelectedIds() {
        Bundle bundle = new Bundle();
        ArrayList<Integer> list = new ArrayList<>();
        for (int position : selectedSet) {
            list.add((Integer) getView(position, null, null).getTag());
        }
        bundle.putIntegerArrayList("measure_ids", list);
        return bundle;
    }

    public int getIdByPosition(int position) {
        return (int) getView(position, null, null).getTag();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        if (selectedSet.contains(position))
            v.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.colorPrimaryDark));
        else
            v.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.colorTransparent));
        return v;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.measure_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        view.setTag(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.ViewMeasures._ID)));
        TextView vName = (TextView) view.findViewById(R.id.measureName);
        TextView vDate = (TextView) view.findViewById(R.id.measureDate);
        TextView vCategory = (TextView) view.findViewById(R.id.measureCategory);
        LinearLayout vBorder = (LinearLayout) view.findViewById(R.id.measureBorder);
        String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ViewMeasures.COLUMN_NAME));
        String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ViewMeasures.COLUMN_CREATION_DATE));
        String category = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ViewMeasures.COLUMN_CATEGORY_NAME));
        String borderColor = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ViewMeasures.COLUMN_CATEGORY_COLOR));
        vName.setText(name);
        vDate.setText(date);
        if (category != null)
            vCategory.setText(category);
        else
            vCategory.setText(view.getResources().getString(R.string.no_category));
        if (borderColor != null)
            vBorder.setBackgroundColor(Color.parseColor(borderColor));
        else
            vBorder.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.colorTransparent));
    }
}

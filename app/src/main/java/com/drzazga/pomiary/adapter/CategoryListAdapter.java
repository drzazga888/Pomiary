package com.drzazga.pomiary.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.drzazga.pomiary.R;
import com.drzazga.pomiary.model.DatabaseContract;

public class CategoryListAdapter extends CursorAdapter {

    private Integer selected = null;

    public void select(int position) {
        if (selected != null && selected.equals(position))
            selected = null;
        else
            selected = position;
        notifyDataSetChanged();
    }

    public CategoryListAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Nullable
    public Integer getSelectedId() {
        if (selected != null)
            return (Integer) getView(selected, null, null).getTag();
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i("aaa", String.valueOf(position));
        View v = super.getView(position, convertView, parent);
        if (selected != null && selected.equals(position))
            v.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.colorPrimaryDark));
        else
            v.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.colorTransparent));
        return v;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.category_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        view.setTag(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.Categories._ID)));
        TextView vName = (TextView) view.findViewById(R.id.categoryName);
        LinearLayout vBorder = (LinearLayout) view.findViewById(R.id.categoryBorder);
        String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Categories.COLUMN_NAME));
        String borderColor = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Categories.COLUMN_COLOR));
        vName.setText(name);
        vBorder.setBackgroundColor(Color.parseColor(borderColor));
    }
}

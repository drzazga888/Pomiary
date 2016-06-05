package com.drzazga.pomiary.adapter;

import android.content.Context;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.drzazga.pomiary.R;
import com.drzazga.pomiary.model.DatabaseContract;

public class CategoryListAdapter extends MultiChoiceCursorAdapter<CategoryListAdapter.CategoryViewHolder> {

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public LinearLayout border;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.categoryName);
            border = (LinearLayout) itemView.findViewById(R.id.categoryBorder);
        }
    }

    public CategoryListAdapter(Context context, Integer id) {
        super(context);
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_item, parent, false);
        return new CategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Log.i("testing", DatabaseUtils.dumpCursorToString(cursor));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Categories.COLUMN_NAME));
        String borderColor = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Categories.COLUMN_COLOR));
        holder.name.setText(name);
        holder.border.setBackgroundColor(Color.parseColor(borderColor));
    }
}

package com.drzazga.pomiary.adapter;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import com.drzazga.pomiary.R;

public abstract class MultiChoiceCursorAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected Cursor cursor;
    protected Context context;
    private SparseBooleanArray selectedItems;
    private Integer selectedItem;
    protected Integer id;
    private CursorAdapter cursorAdapter;

    public MultiChoiceCursorAdapter(Context context) {
        this.context = context;
        this.selectedItems = new SparseBooleanArray();
        this.selectedItem = null;
        this.id = null;
        this.cursorAdapter = new CursorAdapter(context, null, 0) {

            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return null;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {

            }

            @Override
            public void notifyDataSetChanged() {
                super.notifyDataSetChanged();
                MultiChoiceCursorAdapter.this.notifyDataSetChanged();
            }
        };
    }

    public CursorAdapter getCursorAdapter() {
        return cursorAdapter;
    }

    public void toggleSelection(int position) {
        if (selectedItems.get(position))
            selectedItems.delete(position);
        else
            selectedItems.put(position, true);
        notifyItemChanged(position);
    }

    public void clearSelection() {
        selectedItems.clear();
        selectedItem = null;
        notifyDataSetChanged();
    }

    public void toggleSelectionMaxOne(int position) {
        if (selectedItem != null) {
            notifyItemChanged(selectedItem);
            if (selectedItem == position)
                selectedItem = null;
            else {
                selectedItem = position;
                notifyItemChanged(selectedItem);
            }
        } else {
            selectedItem = position;
            notifyItemChanged(selectedItem);
        }
    }

    public boolean isSelected(int position) {
        if (selectedItem != null)
            return selectedItem == position;
        else
            return selectedItems.get(position);
    }

    public Integer getSelectedMaxOne() {
        return selectedItem;
    }

    public int selectedCount() {
        return selectedItems.size();
    }

    public SparseBooleanArray getSelected() {
        return selectedItems;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        cursor = getCursorAdapter().getCursor();
        cursor.moveToPosition(position);
        int currentId = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID));
        if (id != null && id == currentId) {
            selectedItem = holder.getAdapterPosition();
            id = null;
        }
        holder.itemView.setTag(currentId);
        if (isSelected(position))
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorPrimaryDark));
        else
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorTransparent));
    }

    @Override
    public int getItemCount() {
        cursor = getCursorAdapter().getCursor();
        if (cursor == null)
            return 0;
        else
            return cursor.getCount();
    }

    public void selectById(Integer categoryId) {
        id = categoryId;
        notifyDataSetChanged();
    }
}

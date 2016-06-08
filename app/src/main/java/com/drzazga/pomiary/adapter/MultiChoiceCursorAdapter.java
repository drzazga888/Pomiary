package com.drzazga.pomiary.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Handler;
import android.provider.BaseColumns;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.widget.AdapterView;

import com.drzazga.pomiary.R;

public abstract class MultiChoiceCursorAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected AdapterView.OnItemClickListener itemClick;
    protected AdapterView.OnItemLongClickListener longItemClick;
    protected Cursor cursor;
    protected Context context;
    private DataSetObserver mDataSetObserver;
    private SparseBooleanArray selectedItems;
    private Integer selectedItem;
    protected Integer id;

    public MultiChoiceCursorAdapter(Context context) {
        this.cursor = null;
        this.context = context;
        this.mDataSetObserver = new NotifyingDataSetObserver();
        this.selectedItems = new SparseBooleanArray();
        this.selectedItem = null;
        this.id = null;
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

    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == cursor)
            return null;
        final Cursor oldCursor = cursor;
        if (oldCursor != null && mDataSetObserver != null)
            oldCursor.unregisterDataSetObserver(mDataSetObserver);
        cursor = newCursor;
        if (cursor != null && mDataSetObserver != null)
            cursor.registerDataSetObserver(mDataSetObserver);
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
        return oldCursor;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
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
        if (cursor == null)
            return 0;
        else
            return cursor.getCount();
    }

    public void selectById(Integer categoryId) {
        id = categoryId;
        notifyDataSetChanged();
    }

    private class NotifyingDataSetObserver extends DataSetObserver {

        @Override
        public void onChanged() {
            super.onChanged();
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            notifyDataSetChanged();
        }
    }
}

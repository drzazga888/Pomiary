package com.drzazga.pomiary.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class MultiChoiceCursorTouchListener implements RecyclerView.OnItemTouchListener {

    public interface Callbacks {
        void onClick(View v, int position);
        boolean onLongClick(View v, int position);
    }

    private GestureDetector gestureDetector;
    private Callbacks callbacks;

    public MultiChoiceCursorTouchListener(final Callbacks callbacks, Context context, final RecyclerView list) {
        this.callbacks = callbacks;
        this.gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View v = list.findChildViewUnder(e.getX(), e.getY());
                if (v != null && callbacks != null)
                    callbacks.onLongClick(v, list.getChildAdapterPosition(v));
                super.onLongPress(e);
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View v = rv.findChildViewUnder(e.getX(), e.getY());
        if (v != null && callbacks != null && gestureDetector.onTouchEvent(e))
            callbacks.onClick(v, rv.getChildAdapterPosition(v));
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }
}

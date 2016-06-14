package com.drzazga.pomiary.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.drzazga.pomiary.R;
import com.drzazga.pomiary.adapter.MultiChoiceCursorAdapter;
import com.drzazga.pomiary.adapter.MultiChoiceCursorTouchListener;

public abstract class BaseListFragment extends Fragment
        implements MultiChoiceCursorTouchListener.Callbacks, LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView list;
    protected MultiChoiceCursorAdapter adapter;
    protected RecyclerView.LayoutManager layoutManager;
    private NestedScrollView emptyTextWrapper;
    private TextView emptyText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_recycler_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list = (RecyclerView) view.findViewById(android.R.id.list);
        emptyText = (TextView) view.findViewById(R.id.empty_text);
        emptyTextWrapper = (NestedScrollView) view.findViewById(R.id.empty_text_wrapper);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        emptyText.setText(getString(getEmptyListMessage()));
        adapter = instantiateAdapter();
        layoutManager = new LinearLayoutManager(getActivity());
        list.setAdapter(adapter);
        list.setLayoutManager(layoutManager);
        list.addOnItemTouchListener(new MultiChoiceCursorTouchListener(this, getActivity(), list));
        list.setHasFixedSize(true);
        getLoaderManager().initLoader(getLoaderId(), null, this);
    }

    protected abstract int getLoaderId();

    private void setListVisibility(Cursor data) {
        boolean isListEmpty = data == null || data.getCount() == 0;
        if (!isListEmpty) {
            emptyTextWrapper.setVisibility(TextView.GONE);
            list.setVisibility(RecyclerView.VISIBLE);
        } else {
            emptyTextWrapper.setVisibility(TextView.VISIBLE);
            list.setVisibility(RecyclerView.GONE);
        }
    }

    protected abstract MultiChoiceCursorAdapter instantiateAdapter();

    @StringRes
    protected abstract int getEmptyListMessage();

    public RecyclerView.LayoutManager getLayoutManager() {
        return layoutManager;
    }

    public MultiChoiceCursorAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        setListVisibility(data);
        adapter.getCursorAdapter().swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        setListVisibility(null);
        adapter.getCursorAdapter().swapCursor(null);
    }
}
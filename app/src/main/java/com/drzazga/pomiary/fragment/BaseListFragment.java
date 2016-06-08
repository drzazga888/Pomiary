package com.drzazga.pomiary.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.drzazga.pomiary.R;
import com.drzazga.pomiary.activity.OnDemandLoaderRestarter;
import com.drzazga.pomiary.adapter.MultiChoiceCursorAdapter;
import com.drzazga.pomiary.adapter.MultiChoiceCursorTouchListener;
import com.drzazga.pomiary.fragment.dialog.ConfirmActionDialogFragment;

public abstract class BaseListFragment extends Fragment implements ConfirmActionDialogFragment.OnFinishedListener, MultiChoiceCursorTouchListener.Callbacks {

    private RecyclerView list;
    protected MultiChoiceCursorAdapter adapter;
    protected RecyclerView.LayoutManager layoutManager;
    @StringRes int emptyListMessage;
    private NestedScrollView emptyTextWrapper;
    protected OnDemandLoaderRestarter loaderRestarter;
    private Boolean visibilityStatus = null;

    public BaseListFragment() {
        super();
        emptyListMessage = getEmptyListMessage();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_recycler_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list = (RecyclerView) view.findViewById(android.R.id.list);
        TextView emptyText = (TextView) view.findViewById(R.id.empty_text);
        emptyTextWrapper = (NestedScrollView) view.findViewById(R.id.empty_text_wrapper);
        adapter = getAdapter();
        list.setAdapter(adapter);
        list.addOnItemTouchListener(new MultiChoiceCursorTouchListener(this, getActivity(), list));
        layoutManager = new LinearLayoutManager(getActivity());
        list.setLayoutManager(layoutManager);
        list.setHasFixedSize(true);
        emptyText.setText(getString(emptyListMessage));
        loaderRestarter = (OnDemandLoaderRestarter) getActivity();
        if (visibilityStatus != null)
            handleListVisibility();

    }

    private void handleListVisibility() {
        if (visibilityStatus) {
            emptyTextWrapper.setVisibility(TextView.GONE);
            list.setVisibility(RecyclerView.VISIBLE);
        } else {
            emptyTextWrapper.setVisibility(TextView.VISIBLE);
            list.setVisibility(RecyclerView.GONE);
        }
        visibilityStatus = null;
    }

    public void toggleListVisibilityIfNeeded(Cursor data) {
        visibilityStatus = data != null && data.getCount() > 0;
        if (emptyTextWrapper != null && list != null) {
            handleListVisibility();
        }
    }

    protected abstract MultiChoiceCursorAdapter instantiateAdapter();

    @StringRes
    protected abstract int getEmptyListMessage();

    protected abstract void reloadLoaders();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public MultiChoiceCursorAdapter getAdapter() {
        if (adapter == null)
            adapter = instantiateAdapter();
        return adapter;
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return layoutManager;
    }

    @Override
    public void onFinishedDialog() {
        reloadLoaders();
    }
}
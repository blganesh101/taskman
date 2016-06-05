package com.blganesh.taskman.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blganesh.taskman.R;
import com.blganesh.taskman.adapters.Adapter;
import com.blganesh.taskman.util.Displayable;

import java.util.List;


public abstract class BasePickerFragment<T extends Displayable> extends Fragment implements Adapter.OnItemClickedListener<T>, SwipeRefreshLayout.OnRefreshListener {
    protected Adapter<T> mAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_picker_fragment, container, false);
        RecyclerView list = (RecyclerView) view.findViewById(R.id.list);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new Adapter<T>(this);
        list.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mSwipeRefreshLayout.setRefreshing(true);
    }

    public void display(List<T> items) {
        mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.setItems(items);
        mAdapter.notifyDataSetChanged();
    }
}

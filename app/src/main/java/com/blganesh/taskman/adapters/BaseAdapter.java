package com.blganesh.taskman.adapters;
/**
 * Created by ganeshbanda on 05/06/16.
 */
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    protected List<T> mItems = new ArrayList<>();

    protected abstract void onBindItem(VH holder, T item, int position);

    @Override
    public void onBindViewHolder(VH holder, int position) {
        onBindItem(holder, mItems.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    protected T getItem(int position) {
        return mItems.get(position);
    }

    public void setItems(List<T> items) {
        mItems = items;
    }
}

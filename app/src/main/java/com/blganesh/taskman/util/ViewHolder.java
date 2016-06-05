package com.blganesh.taskman.util;
/**
 * Created by ganeshbanda on 05/06/16.
 */
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.blganesh.taskman.R;

public final class ViewHolder extends RecyclerView.ViewHolder {
    public TextView text;

    public ViewHolder(View view) {
        super(view);
        text = (TextView) view.findViewById(R.id.text);
    }
}

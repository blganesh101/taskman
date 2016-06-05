package com.blganesh.taskman.trello;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blganesh.taskman.R;

final class ViewHolder extends RecyclerView.ViewHolder {
    public final TextView name;
    public final TextView timeSpent;
    @Nullable public ImageView start;

    public ViewHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.text);
        timeSpent = (TextView) itemView.findViewById(R.id.time_spent);
        start = (ImageView) itemView.findViewById(R.id.start);
    }
}

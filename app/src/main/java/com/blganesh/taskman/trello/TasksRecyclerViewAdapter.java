package com.blganesh.taskman.trello;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blganesh.taskman.BaseApplication;
import com.blganesh.taskman.R;
import com.blganesh.taskman.adapters.BaseAdapter;
import com.blganesh.taskman.tasks.Task;

public final class TasksRecyclerViewAdapter extends BaseAdapter<Card, ViewHolder> {
    public interface OnTimerClickedListener {
        void onTimerClicked(Card card);
    }

    private static final String TAG = "TasksRecyclerViewAdapter";

    private final OnTimerClickedListener mOnTimerClickedListener;
    private boolean mTimerEnabled = true;

    public TasksRecyclerViewAdapter(OnTimerClickedListener onTimerClickedListener) {
        mOnTimerClickedListener = onTimerClickedListener;
    }

    public void setTimerEnabled(boolean enabled) {
        mTimerEnabled = enabled;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(mTimerEnabled ? R.layout.tasks_fragment_item : R.layout.tasks_fragment_item_no_timer,
                parent,
                false);
        final ViewHolder holder = new ViewHolder(view);

        if (holder.start != null) {
            holder.start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = holder.getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        Card card = getItem(position);
                        mOnTimerClickedListener.onTimerClicked(card);
                    }
                }
            });
        }
        return holder;
    }

    @Override
    protected void onBindItem(ViewHolder holder, Card card, int position) {
        holder.name.setText(card.getName());
        Task task = BaseApplication.get().getTaskCache().get(card.getId());
        String timeSpent = "";

        if (task != null) {
            timeSpent = task.getPrettyTotalTime();
        }

        holder.timeSpent.setText(timeSpent);

        if (holder.start != null) {
            holder.start.setVisibility(mTimerEnabled ? View.VISIBLE : View.INVISIBLE);
        }
    }
}

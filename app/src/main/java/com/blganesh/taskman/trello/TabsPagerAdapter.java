package com.blganesh.taskman.trello;

/**
 * Created by ganeshbanda on 05/06/16.
 */
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.blganesh.taskman.R;
import com.blganesh.taskman.fragments.DoingTasksFragment;
import com.blganesh.taskman.fragments.DoneTasksFragment;
import com.blganesh.taskman.fragments.TodoTasksFragment;
import com.blganesh.taskman.tasks.TaskManager;

public final class TabsPagerAdapter extends FragmentPagerAdapter {
    private final Context mContext;

    public TabsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context.getApplicationContext();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return TodoTasksFragment.newInstance(TaskManager.getToDoListId(mContext));
            case 1:
                return DoingTasksFragment.newInstance(TaskManager.getDoingListId(mContext));
            case 2:
                return DoneTasksFragment.newInstance(TaskManager.getDoneListId(mContext));
        }

        throw new AssertionError();
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getStringArray(R.array.tasks_tabs_titles)[position];
    }
}

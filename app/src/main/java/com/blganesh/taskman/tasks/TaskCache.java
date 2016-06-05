package com.blganesh.taskman.tasks;

/**
 * Created by ganeshbanda on 05/06/16.
 */
import android.support.annotation.Nullable;
import android.support.v4.util.SimpleArrayMap;

public final class TaskCache {
    private SimpleArrayMap<String, Task> mTasks;

    public TaskCache() {
    }

    public void setTasks(SimpleArrayMap<String, Task> tasks) {
        mTasks = tasks;
    }

    @Nullable
    public Task get(String cardId) {
        if (mTasks == null) {
            return null;
        }

        return mTasks.get(cardId);
    }
}


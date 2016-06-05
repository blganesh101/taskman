package com.blganesh.taskman.loaders;

/**
 * Created by ganeshbanda on 05/06/16.
 */
import android.content.Context;
import android.support.v4.util.SimpleArrayMap;

import com.blganesh.taskman.tasks.Task;
import com.blganesh.taskman.tasks.TaskManager;


public final class TaskLoader extends BaseLoader<SimpleArrayMap<String, Task>> {
    private static final String TAG = "TasksLoader";

    public TaskLoader(Context context) {
        super(context);
    }

    @Override
    public SimpleArrayMap<String, Task> loadInBackground() {
        return TaskManager.getTasks(getContext());
    }
}

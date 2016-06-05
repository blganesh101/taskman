package com.blganesh.taskman.loaders;

/**
 * Created by ganeshbanda on 05/06/16.
 */
import android.content.Context;
import android.support.v4.util.SimpleArrayMap;

import com.blganesh.taskman.BaseApplication;
import com.blganesh.taskman.tasks.Task;
import com.blganesh.taskman.tasks.TaskManager;

public final class UpdateTasksLoader extends BaseLoader<Boolean> {
    private static final String TAG = "UpdateTaskLoader";
    private final String mCardId;
    private final long mTime;
    private final boolean mIncPomodoros;

    public UpdateTasksLoader(Context context, String cardId, long time, boolean incPomodoros) {
        super(context);
        mCardId = cardId;
        mTime = time;
        mIncPomodoros = incPomodoros;
    }

    @Override
    public Boolean loadInBackground() {
        SimpleArrayMap<String, Task> tasks = TaskManager.getTasks(getContext());

        if (tasks != null) {
            Task task = tasks.get(mCardId);

            if (task == null) {
                tasks.put(mCardId, new Task(
                        mCardId,
                        mIncPomodoros ? 1 : 0,
                        mTime
                ));
            } else {
                int pomodoros = task.getPodomoros();

                if (mIncPomodoros) {
                    ++pomodoros;
                }

                tasks.put(mCardId, new Task(
                        task.getCardId(),
                        pomodoros,
                        task.getTotalTime() + mTime
                ));
            }

            BaseApplication.get().getTaskCache().setTasks(tasks);
            return TaskManager.saveTasks(getContext(), tasks);
        }

        return Boolean.FALSE;
    }
}

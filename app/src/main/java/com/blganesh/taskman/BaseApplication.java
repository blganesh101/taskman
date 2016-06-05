package com.blganesh.taskman;

/**
 * Created by ganeshbanda on 05/06/16.
 */
import android.app.Application;
import com.blganesh.taskman.tasks.TaskCache;

public final class BaseApplication extends Application {
    private static BaseApplication sApp;

    private TaskCache mTasksCache = new TaskCache();

    public static BaseApplication get() {
        return sApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
    }

    public TaskCache getTaskCache() {
        return mTasksCache;
    }
}

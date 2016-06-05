package com.blganesh.taskman.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.util.SimpleArrayMap;
import android.text.TextUtils;

import com.blganesh.taskman.BaseApplication;
import com.blganesh.taskman.users.TrelloKey;
import com.blganesh.taskman.tasks.Task;
import com.blganesh.taskman.loaders.TaskLoader;
import com.blganesh.taskman.R;
import com.blganesh.taskman.TaskManMainActivity;

public final class SplashActivity extends TaskManMainActivity implements Handler.Callback, LoaderManager.LoaderCallbacks<SimpleArrayMap<String, Task>> {
    private static final int WHAT_CONTINUE = 1;
    private static final String STATE_REMAINING = "remaining";
    private static final String STATE_TASKS_LOADED = "tasksLoaded";
    private static final int TASKS_LOADER_ID = 1;

    private Handler mHandler;
    private long mRemaining = 2 * 1000L; // Minimum duration during which the splash should be shown
    private long mLastTick;
    private boolean mTasksLoaded = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (savedInstanceState != null) {
            mRemaining = savedInstanceState.getLong(STATE_REMAINING);
            mTasksLoaded = savedInstanceState.getBoolean(STATE_TASKS_LOADED);
        }

        mHandler = new Handler(this);
        getSupportLoaderManager().initLoader(TASKS_LOADER_ID, null, this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mRemaining > 0L) {
            mHandler.sendEmptyMessageDelayed(WHAT_CONTINUE, mRemaining);
        } else {
            launchActivity(this);
        }

        mLastTick = SystemClock.elapsedRealtime();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHandler.removeCallbacksAndMessages(null);
        long currentTick = SystemClock.elapsedRealtime();
        mRemaining -= (SystemClock.elapsedRealtime() - mLastTick);
        mLastTick = currentTick;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        long currentTick = SystemClock.elapsedRealtime();
        mRemaining -= (SystemClock.elapsedRealtime() - mLastTick);
        mLastTick = currentTick;
        outState.putLong(STATE_REMAINING, mRemaining);
        outState.putBoolean(STATE_TASKS_LOADED, mTasksLoaded);
    }

    @Override
    public boolean handleMessage(Message message) {
        switch (message.what) {
            case WHAT_CONTINUE:
                if (mTasksLoaded) {
                    launchActivity(this);
                }

                return true;
            default:
                return false;
        }
    }

    @Override
    public Loader<SimpleArrayMap<String, Task>> onCreateLoader(int id, Bundle args) {
        return new TaskLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<SimpleArrayMap<String, Task>> loader, SimpleArrayMap<String, Task> tasks) {
        if (tasks != null) {
            BaseApplication.get().getTaskCache().setTasks(tasks);
        }

        mTasksLoaded = true;

        if (mRemaining < 0L) {
            launchActivity(this);
        }
    }

    @Override
    public void onLoaderReset(Loader<SimpleArrayMap<String, Task>> loader) {
    }

    private static void launchActivity(Activity activity) {
        if (TextUtils.isEmpty(TrelloKey.getPersistedToken(activity))) {
            activity.startActivity(new Intent(activity, TrelloWebLoginActivity.class));
        } else if (TrelloLoginActivity.trelloFullySetup(activity)) {
            activity.startActivity(new Intent(activity, TrelloPomoActivity.class));
        } else {
            activity.startActivity(new Intent(activity, TrelloLoginActivity.class));
        }

        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}


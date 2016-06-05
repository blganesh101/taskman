package com.blganesh.taskman.tasks;

/**
 * Created by ganeshbanda on 05/06/16.
 */
import android.content.Context;
import android.support.annotation.CheckResult;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.support.v4.util.SimpleArrayMap;
import android.text.TextUtils;
import android.util.Log;


import org.json.JSONException;

public final class TaskManager {
    private static final String TAG = "Persistence";
    private static final String PREF_NAME = "persistence";
    private static final String KEY_BOARD_ID = "boardId";
    private static final String KEY_TODO_LIST_ID = "todoListId";
    private static final String KEY_DOING_LIST_ID = "doingListId";
    private static final String KEY_DONE_LIST_ID = "doneListId";
    private static final String KEY_TASKS = "tasks";

    private TaskManager() {
    }

    @CheckResult
    public static boolean saveBoardId(Context context, String id) {
        return saveString(context, KEY_BOARD_ID, id);
    }

    public static String getBoardId(Context context) {
        return getString(context, KEY_BOARD_ID);
    }

    @CheckResult
    public static boolean saveToDoListId(Context context, String id) {
        return saveString(context, KEY_TODO_LIST_ID, id);
    }

    public static String getToDoListId(Context context) {
        return getString(context, KEY_TODO_LIST_ID);
    }

    @CheckResult
    public static boolean saveDoingListId(Context context, String id) {
        return saveString(context, KEY_DOING_LIST_ID, id);
    }

    public static String getDoingListId(Context context) {
        return getString(context, KEY_DOING_LIST_ID);
    }

    @CheckResult
    public static boolean saveDoneListId(Context context, String id) {
        return saveString(context, KEY_DONE_LIST_ID, id);
    }

    public static String getDoneListId(Context context) {
        return getString(context, KEY_DONE_LIST_ID);
    }

    @CheckResult
    public static boolean saveTasks(Context context, SimpleArrayMap<String, Task> tasks) {
        try {
            return saveString(context, KEY_TASKS, TaskPersistence.toJson(tasks));
        } catch (JSONException e) {
            Log.e(TAG, String.valueOf(e));
            return false;
        }
    }

    @Nullable
    @WorkerThread
    public static SimpleArrayMap<String, Task> getTasks(Context context) {
        Log.d(TAG, "Loading tasks");

        try {
            SimpleArrayMap<String, Task> ret;
            String json = getString(context, KEY_TASKS);

            if (TextUtils.isEmpty(json)) {
                ret = new SimpleArrayMap<>();
            } else {
                ret = TaskPersistence.fromJson(json);
            }

            Log.d(TAG, "Loaded tasks " + ret);
            return ret;
        } catch (JSONException e) {
            Log.e(TAG, String.valueOf(e));
            return null;
        }
    }

    @CheckResult
    @WorkerThread
    private static boolean saveString(Context context, String key, String value) {
        Log.d(TAG, "Saved string " + value + " for key " + key);
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString(key, value)
                .commit();
    }

    private static String getString(Context context, String key) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getString(key, null);
    }
}


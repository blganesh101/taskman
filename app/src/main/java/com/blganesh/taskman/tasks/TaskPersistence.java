package com.blganesh.taskman.tasks;

/**
 * Created by ganeshbanda on 05/06/16.
 */
import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

final class TaskPersistence {
    private static final String TAG = "TasksPersistence";
    private static final String KEY_TASKS = "tasks";
    private static final String KEY_CARD_ID = "cardId";
    private static final String KEY_POMODOROS = "pomodoros";
    private static final String KEY_TOTAL_TIME = "totalTime";

    private TaskPersistence() {
    }

    public static String toJson(SimpleArrayMap<String, Task> tasks)
            throws JSONException {
        JSONObject root = new JSONObject();
        JSONObject jsonTasks = new JSONObject();

        for (int i = 0; i < tasks.size(); ++i) {
            String cardId = tasks.keyAt(i);
            Task task = tasks.valueAt(i);
            jsonTasks.put(cardId, toJson(task));
        }

        root.put(KEY_TASKS, jsonTasks);
        String json = root.toString();
        Log.d(TAG, "Generated tasks JSON " + json);
        return json;
    }

    public static SimpleArrayMap<String, Task> fromJson(String json)
            throws JSONException {
        Log.d(TAG, "Parsing " + json);
        JSONObject root = new JSONObject(json);
        JSONObject jsonTasks = root.getJSONObject(KEY_TASKS);
        SimpleArrayMap<String, Task> tasks = new SimpleArrayMap<>();
        Iterator<String> iterator = jsonTasks.keys();

        while (iterator.hasNext()) {
            String cardId = iterator.next();
            JSONObject jsonTask = jsonTasks.getJSONObject(cardId);
            tasks.put(cardId, fromJson(cardId, jsonTask));
        }

        return tasks;
    }

    @NonNull
    private static JSONObject toJson(@NonNull Task task)
            throws JSONException {
        JSONObject jsonTask = new JSONObject();
        jsonTask.put(KEY_POMODOROS, task.getPodomoros());
        jsonTask.put(KEY_TOTAL_TIME, task.getTotalTime());
        return jsonTask;
    }

    @NonNull
    private static Task fromJson(@NonNull String cardId, @NonNull JSONObject jsonTask)
            throws JSONException {
        return new Task(cardId,
                jsonTask.getInt(KEY_POMODOROS),
                jsonTask.getLong(KEY_TOTAL_TIME));
    }
}


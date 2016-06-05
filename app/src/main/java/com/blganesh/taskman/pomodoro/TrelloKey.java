package com.blganesh.taskman.pomodoro;

/**
 * Created by ganeshbanda on 05/06/16.
 */
import android.content.Context;
import android.support.annotation.CheckResult;

public final class TrelloKey {
    private static final String PREF_NAME = "trelloToken";
    private static final String KEY_TOKEN = "token";

    private TrelloKey() {
    }

    @CheckResult
    public static boolean persistToken(Context context, String token) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString(KEY_TOKEN, token)
                .commit();
    }

    public static String getPersistedToken(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getString(KEY_TOKEN, null);
    }

    public static String getAppKey() {
        return "6ea4c21c2d0d193dbfab3c233f4ea1f4";
    }
}


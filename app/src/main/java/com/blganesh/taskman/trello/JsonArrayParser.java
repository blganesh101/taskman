package com.blganesh.taskman.trello;

import android.support.annotation.Nullable;
import android.util.Log;


import com.blganesh.taskman.util.SoundUtils;
import com.blganesh.taskman.util.StreamUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

public abstract class JsonArrayParser<T> implements Parser<T> {
    private static final String TAG = "JsonParser";

    abstract T parseJson(JSONArray root) throws JSONException, ParseException;

    @Nullable
    @Override
    public T parse(InputStream inputStream) {
        T parsed = null;

        try {
            JSONArray root = new JSONArray(StreamUtils.inputStreamToString(inputStream));
            parsed = parseJson(root);
        } catch (IOException|JSONException|ParseException e) {
            Log.e(TAG, String.valueOf(e));
        }

        return parsed;
    }
}

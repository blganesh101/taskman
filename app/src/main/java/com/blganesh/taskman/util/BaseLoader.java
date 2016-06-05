package com.blganesh.taskman.util;

/**
 * Created by ganeshbanda on 05/06/16.
 */
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public abstract class BaseLoader<T> extends AsyncTaskLoader<T> {
    public BaseLoader(Context context) {
        super(context);
    }

    @Override
    public void onStartLoading() {
        forceLoad();
    }

    @Override
    public void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();
    }
}

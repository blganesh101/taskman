package com.blganesh.taskman.trello;

import android.content.Context;
import android.util.Log;

import com.blganesh.taskman.users.TrelloKey;
import com.blganesh.taskman.loaders.BaseLoader;

import java.io.IOException;

public final class AddCommentLoader extends BaseLoader<Boolean> {
    private static final String TAG = "AddCommentLoader";
    private final String mCardId;
    private final String mText;

    public AddCommentLoader(Context context, String cardId, String text) {
        super(context);
        mCardId = cardId;
        mText = text;
    }

    @Override
    public Boolean loadInBackground() {
        try {
            Webservices.addComment(TrelloKey.getAppKey(),
                    TrelloKey.getPersistedToken(getContext()),
                    mCardId,
                    mText);
            return true;
        } catch (IOException e) {
            Log.e(TAG, String.valueOf(e));
            return false;
        }
    }
}

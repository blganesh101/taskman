package com.blganesh.taskman.trello;

import android.content.Context;
import android.util.Log;

import com.blganesh.taskman.users.TrelloKey;
import com.blganesh.taskman.loaders.BaseLoader;

import java.io.IOException;

public final class MoveCardLoader extends BaseLoader<Boolean> {
    private static final String TAG = "MoveCardLoader";
    private final String mCardId;
    private final String mListId;

    public MoveCardLoader(Context context, String cardId, String listId) {
        super(context);
        mCardId = cardId;
        mListId = listId;
    }

    @Override
    public Boolean loadInBackground() {
        try {
            Webservices.moveCard(TrelloKey.getAppKey(),
                    TrelloKey.getPersistedToken(getContext()),
                    mCardId,
                    mListId);
            return true;
        } catch (IOException e) {
            Log.e(TAG, String.valueOf(e));
            return false;
        }
    }
}

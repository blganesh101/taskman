package com.blganesh.taskman.trello;

import android.content.Context;
import android.util.Log;

import com.blganesh.taskman.users.TrelloKey;
import com.blganesh.taskman.loaders.BaseLoader;

import java.io.IOException;
import java.util.List;

public final class CardsLoader extends BaseLoader<List<Card>> {
    private static final String TAG = "CardsLoader";
    private final String mListId;

    public CardsLoader(Context context, String listId) {
        super(context);
        mListId = listId;
    }

    @Override
    public List<Card> loadInBackground() {
        try {
            return Webservices.getListsCards(TrelloKey.getAppKey(),
                    TrelloKey.getPersistedToken(getContext()),
                    mListId);
        } catch (IOException e) {
            Log.e(TAG, String.valueOf(e));
            return null;
        }
    }
}

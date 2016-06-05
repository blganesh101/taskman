package com.blganesh.taskman.trello;

import android.content.Context;
import android.util.Log;

import com.blganesh.taskman.users.TrelloKey;
import com.blganesh.taskman.loaders.BaseLoader;

import java.io.IOException;
import java.util.List;

/**
 * Created by ganeshbanda on 05/06/16.
 */
public final class BoardsLoader extends BaseLoader<List<Board>> {
    private static final String TAG = "BoardsLoader";

    public BoardsLoader(Context context) {
        super(context);
    }

    @Override
    public List<Board> loadInBackground() {
        try {
            return Webservices.getUsersBoards(TrelloKey.getAppKey(), TrelloKey.getPersistedToken(getContext()));
        } catch (IOException e) {
            Log.e(TAG, String.valueOf(e));
            return null;
        }
    }
}

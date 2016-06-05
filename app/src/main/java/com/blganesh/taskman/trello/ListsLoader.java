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
public final class ListsLoader extends BaseLoader<List<BoardList>> {
    private static final String TAG = "ListsLoader";
    private final String mBoardId;

    public ListsLoader(Context context, String boardId) {
        super(context);
        mBoardId = boardId;
    }

    @Override
    public List<BoardList> loadInBackground() {
        try {
            return Webservices.getBoardsLists(TrelloKey.getAppKey(),
                    TrelloKey.getPersistedToken(getContext()),
                    mBoardId);
        } catch (IOException e) {
            Log.e(TAG, String.valueOf(e));
            return null;
        }
    }
}

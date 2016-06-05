package com.blganesh.taskman.trello;

import android.net.Uri;
import android.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class Webservices {
    private static final String TAG = "Webservices";

    private Webservices() {
    }

    public static List<Board> getUsersBoards(String appKey, String token)
            throws IOException {
        String url = getBaseUrlBuilder(appKey, token)
                .path("1/members/me/boards")
                .build()
                .toString();
        return new BoardsArrayParser().parse(Http.get(url));
    }

    public static List<BoardList> getBoardsLists(String appKey, String token, String boardId)
            throws IOException {
        String url = getBaseUrlBuilder(appKey, token)
                .path("1/boards/" + boardId + "/lists")
                .build()
                .toString();
        return new ListsArrayParser().parse(Http.get(url));
    }

    public static List<Card> getListsCards(String appKey, String token, String listId)
            throws IOException {
        String url = getBaseUrlBuilder(appKey, token)
                .path("1/lists/" + listId + "/cards")
                .build()
                .toString();
        return new CardsArrayParser().parse(Http.get(url));
    }

    public static void moveCard(String appKey, String token, String cardId, String listId)
            throws IOException {
        String url = getBaseUrlBuilder(appKey, token)
                .path("1/cards/" + cardId + "/idList")
                .build()
                .toString();
        List<Pair<String, String>> args = new ArrayList<>();
        args.add(Pair.create("value", listId));
        Http.put(url, args).close();
    }

    public static void addComment(String appKey, String token, String cardId, String text)
            throws IOException {
        String url = getBaseUrlBuilder(appKey, token)
                .path("1/cards/" + cardId + "/actions/comments")
                .build()
                .toString();
        List<Pair<String, String>> args = new ArrayList<>();
        args.add(Pair.create("text", text));
        Http.post(url, args).close();
    }

    private static Uri.Builder getBaseUrlBuilder(String appKey, String token) {
        return new Uri.Builder()
                .scheme("https")
                .authority("api.trello.com")
                .appendQueryParameter("key", appKey)
                .appendQueryParameter("token", token);
    }
}

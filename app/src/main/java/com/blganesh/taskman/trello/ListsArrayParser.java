package com.blganesh.taskman.trello;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ListsArrayParser extends JsonArrayParser<List<BoardList>> {
    @Override
    List<BoardList> parseJson(JSONArray root) throws JSONException, ParseException {
        List<BoardList> lists = new ArrayList<>();

        for (int i = 0; i < root.length(); ++i) {
            JSONObject listJson = root.getJSONObject(i);
            lists.add(new BoardList(listJson.getString("id"),
                    listJson.getString("name")));
        }

        return lists;
    }
}

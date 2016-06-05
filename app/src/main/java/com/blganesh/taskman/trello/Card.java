package com.blganesh.taskman.trello;

/**
 * Created by ganeshbanda on 05/06/16.
 */
public final class Card {
    private final String mId;
    private final String mName;

    public Card(String id, String name) {
        mId = id;
        mName = name;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id='" + mId + '\'' +
                ", name='" + mName + '\'' +
                '}';
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }
}

package com.blganesh.taskman.fragments;

import com.blganesh.taskman.activities.TrelloLoginActivity;
import com.blganesh.taskman.trello.BoardList;
import com.blganesh.taskman.util.OnItemPickedListener;

public final class DoingListPickerFragment extends BasePickerFragment<BoardList> {
    public static DoingListPickerFragment newInstance() {
        return new DoingListPickerFragment();
    }

    @Override
    public void onStart() {
        super.onStart();
        onRefresh();
    }

    @Override
    public void onItemClicked(BoardList list) {
        // See comment in BoardPickerFragment
        OnItemPickedListener l = (OnItemPickedListener) getActivity();

        l.onDoingListPicked(list);
    }

    @Override
    public void onRefresh() {
        ((TrelloLoginActivity) getActivity()).requestDoing();
    }
}

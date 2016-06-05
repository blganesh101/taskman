package com.blganesh.taskman.fragments;

import com.blganesh.taskman.activities.TrelloLoginActivity;
import com.blganesh.taskman.trello.BoardList;
import com.blganesh.taskman.util.OnItemPickedListener;

public final class DoneListPickerFragment extends BasePickerFragment<BoardList> {
    public static DoneListPickerFragment newInstance() {
        return new DoneListPickerFragment();
    }

    @Override
    public void onItemClicked(BoardList list) {
        // See comment in BoardPickerFragment
        OnItemPickedListener l = (OnItemPickedListener) getActivity();

        l.onDoneListPicked(list);
    }

    @Override
    public void onRefresh() {
        ((TrelloLoginActivity) getActivity()).requestDone();
    }
}

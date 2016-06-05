package com.blganesh.taskman.fragments;

import com.blganesh.taskman.activities.TrelloLoginActivity;
import com.blganesh.taskman.trello.BoardList;
import com.blganesh.taskman.util.OnItemPickedListener;

public final class ToDoListPickerFragment extends BasePickerFragment<BoardList> {
    public static ToDoListPickerFragment newInstance() {
        return new ToDoListPickerFragment();
    }

    @Override
    public void onStart() {
        super.onStart();
        onRefresh();
    }

    @Override
    public void onItemClicked(BoardList list) {
        // We want the app to crash if the activity doesn't implement this, it's not recoverable
        // and has to be caught during development
        OnItemPickedListener l = (OnItemPickedListener) getActivity();

        l.onTodoListPicked(list);
    }

    @Override
    public void onRefresh() {
        ((TrelloLoginActivity) getActivity()).requestToDo();
    }
}

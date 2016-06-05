package com.blganesh.taskman.fragments;


import com.blganesh.taskman.activities.TrelloLoginActivity;
import com.blganesh.taskman.trello.Board;
import com.blganesh.taskman.util.OnItemPickedListener;

public final class BoardPickerFragment extends BasePickerFragment<Board> {
    public static BoardPickerFragment newInstance() {
        return new BoardPickerFragment();
    }

    @Override
    public void onStart() {
        super.onStart();
        onRefresh();
    }

    @Override
    public void onItemClicked(Board board) {
        // We want the app to crash if the activity doesn't implement this, it's not recoverable
        // and has to be caught during development
        OnItemPickedListener l = (OnItemPickedListener) getActivity();

        l.onBoardPicked(board);
    }

    @Override
    public void onRefresh() {
        ((TrelloLoginActivity) getActivity()).requestBoards();
    }
}

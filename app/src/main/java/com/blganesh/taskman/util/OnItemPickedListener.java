package com.blganesh.taskman.util;

/**
 * Created by ganeshbanda on 05/06/16.
 */
import com.blganesh.taskman.trello.Board;
import com.blganesh.taskman.trello.BoardList;

public interface OnItemPickedListener {
    void onBoardPicked(Board board);
    void onTodoListPicked(BoardList list);
    void onDoingListPicked(BoardList list);
    void onDoneListPicked(BoardList list);
}

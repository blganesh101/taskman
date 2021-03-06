package com.blganesh.taskman.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blganesh.taskman.BaseApplication;
import com.blganesh.taskman.R;
import com.blganesh.taskman.activities.TaskTimerActivity;
import com.blganesh.taskman.trello.Card;
import com.blganesh.taskman.trello.CardsLoader;
import com.blganesh.taskman.trello.ExclusiveLayout;
import com.blganesh.taskman.trello.MoveCardLoader;
import com.blganesh.taskman.trello.TasksRecyclerViewAdapter;
import com.blganesh.taskman.adapters.BaseAdapter;
import com.blganesh.taskman.tasks.Task;
import com.blganesh.taskman.tasks.TaskManager;

import java.util.List;

abstract class TasksFragment extends Fragment implements TasksRecyclerViewAdapter.OnTimerClickedListener, SwipeRefreshLayout.OnRefreshListener, LoaderManager.LoaderCallbacks<List<Card>> {
    protected static final String ARG_LIST_ID = "listId";
    private static final int CARDS_LOADER_ID = 1;
    private static final int MOVE_CARD_LOADER_ID = 2;

    private BaseAdapter<Card, ? extends RecyclerView.ViewHolder> mAdapter;
    private View mRoot;
    private ExclusiveLayout mExclusiveLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    protected BaseAdapter<Card, ? extends RecyclerView.ViewHolder> createAdapter() {
        return new TasksRecyclerViewAdapter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tasks_fragment, container, false);
        mExclusiveLayout = (ExclusiveLayout) view.findViewById(R.id.exclusive_layout);
        mRoot = mExclusiveLayout;
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        RecyclerView list = (RecyclerView) view.findViewById(R.id.list);
        mAdapter = createAdapter();
        list.setAdapter(mAdapter);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mExclusiveLayout.showFirst();
        onRefresh();
    }

    @Override
    public void onTimerClicked(Card card){
        Task task = BaseApplication.get().getTaskCache().get(card.getId());
        getLoaderManager().initLoader(MOVE_CARD_LOADER_ID, null, new MoveCardLoaderCallbacks(card.getId(), TaskManager.getDoingListId(getContext())));
        Intent intent = new Intent(getContext(), TaskTimerActivity.class);
        TaskTimerActivity.fillIntent(intent, card.getId(), task);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        getLoaderManager().initLoader(CARDS_LOADER_ID, null, TasksFragment.this);
    }

    @Override
    public Loader<List<Card>> onCreateLoader(int i, Bundle bundle) {
        return new CardsLoader(getContext(), getArguments().getString(ARG_LIST_ID));
    }

    @Override
    public void onLoadFinished(Loader<List<Card>> loader, List<Card> cards) {
        mSwipeRefreshLayout.setRefreshing(false);
        getLoaderManager().destroyLoader(CARDS_LOADER_ID);

        if (cards == null) {
            Snackbar.make(mRoot, R.string.error_loading_cards, Snackbar.LENGTH_LONG).show();
        } else if (cards.size() == 0) {
            mExclusiveLayout.showNext();
        } else {
            mExclusiveLayout.showNext();
            mAdapter.setItems(cards);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Card>> loader) {
    }

    private final class MoveCardLoaderCallbacks implements LoaderManager.LoaderCallbacks<Boolean> {
        private final String mCardId;
        private final String mListId;

        public MoveCardLoaderCallbacks(String cardId, String listId) {
            this.mCardId = cardId;
            this.mListId = listId;
        }

        @Override
        public Loader<Boolean> onCreateLoader(int id, Bundle args) {
            return new MoveCardLoader(getContext(), mCardId, mListId);
        }

        @Override
        public void onLoadFinished(Loader<Boolean> loader, Boolean data) {
            getLoaderManager().destroyLoader(MOVE_CARD_LOADER_ID);

            if (!data) {
                Snackbar.make(mRoot, R.string.error_card_move, Snackbar.LENGTH_LONG).show();
            }
        }

        @Override
        public void onLoaderReset(Loader<Boolean> loader) {
        }
    }
}

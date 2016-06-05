package com.blganesh.taskman.activities;

/**
 * Created by ganeshbanda on 05/06/16.
 */
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blganesh.taskman.R;
import com.blganesh.taskman.TaskManMainActivity;
import com.blganesh.taskman.trello.AddCommentLoader;
import com.blganesh.taskman.trello.MoveCardLoader;
import com.blganesh.taskman.timers.AlarmReceiver;
import com.blganesh.taskman.util.SoundUtils;
import com.blganesh.taskman.tasks.Task;
import com.blganesh.taskman.tasks.TaskManager;
import com.blganesh.taskman.util.TimeActivityPresenceIndicator;
import com.blganesh.taskman.fragments.TimesUpDialogFragment;
import com.blganesh.taskman.loaders.UpdateTasksLoader;

import java.util.Locale;

public final class TaskTimerActivity extends TaskManMainActivity implements View.OnClickListener, Handler.Callback {
    public static final String EXTRA_SKIP_NEXT_SOUND = "skipNextSound";

    private static final String TAG = "TimerActivity";
    private static final String EXTRA_CARD_ID = "cardId";
    private static final String EXTRA_TASK = "task";
    private static final int WHAT_UPDATE_TIMER = 1;
    private static final long REFRESH_DELAY = 500L;

    private static final String STATE_CARD_ID = "cardId";
    private static final String STATE_TASK = "task";
    private static final String STATE_RUNNING = "running";
    private static final String STATE_REMAINING_TIME = "remainingTime";
    private static final String STATE_LAST_TICK = "lastTick";
    private static final String STATE_SKIP_NEXT_SOUND = "skipNextSound";

    private static final int TASK_UPDATE_LOADER_ID = 1;
    private static final int MOVE_CARD_LOADER_ID = 2;
    private static final int ADD_COMMENT_LOADER_ID = 3;

    private Handler mHandler;
    private TimeActivityPresenceIndicator mTimeActivityPresenceIndicator = new TimeActivityPresenceIndicator();

    private String mCardId;
    private Task mTask;
    private boolean mRunning;
    private long mRemainingTime;
    private long mLastTick;
    private boolean mSkipNextSound;

    private CoordinatorLayout mCoordinator;
    private TextView mTime;
    private Button mPauseResume;
    private Button mStop;

    public static void fillIntent(Intent intent, String cardId, Task task) {
        intent.putExtra(EXTRA_CARD_ID, cardId);
        intent.putExtra(EXTRA_TASK, task);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mSkipNextSound = intent.getBooleanExtra(EXTRA_SKIP_NEXT_SOUND, false);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_activity);
        setupToolbar();

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            mCardId = intent.getStringExtra(EXTRA_CARD_ID);
            mTask = intent.getParcelableExtra(EXTRA_TASK);

            if (mTask == null) {
                mTask = new Task(mCardId, 0, 0L);
            }

            mRunning = true;
            mRemainingTime = Task.POMODORO_DURATION;
            mLastTick = SystemClock.elapsedRealtime();
            mSkipNextSound = intent.getBooleanExtra(EXTRA_SKIP_NEXT_SOUND, false);
            setupAlarm();
        } else {
            mCardId = savedInstanceState.getString(STATE_CARD_ID);
            mTask = savedInstanceState.getParcelable(STATE_TASK);
            mRunning = savedInstanceState.getBoolean(STATE_RUNNING);
            mRemainingTime = savedInstanceState.getLong(STATE_REMAINING_TIME);
            mLastTick = savedInstanceState.getLong(STATE_LAST_TICK);
            mSkipNextSound = savedInstanceState.getBoolean(STATE_SKIP_NEXT_SOUND);
        }

        mCoordinator = (CoordinatorLayout) findViewById(R.id.coordinator);
        mTime = (TextView) findViewById(R.id.time);
        mPauseResume = (Button) findViewById(R.id.pause_resume);
        mPauseResume.setOnClickListener(this);
        mStop = (Button) findViewById(R.id.stop);
        mStop.setOnClickListener(this);
        mHandler = new Handler(this);
        updateTimer();
        updateButtons();
    }

    @Override
    public void onStart() {
        super.onStart();
        mTimeActivityPresenceIndicator.onStart(this);

        if (mRunning) {
            mHandler.sendEmptyMessage(WHAT_UPDATE_TIMER);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mTimeActivityPresenceIndicator.onStop(this);
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_CARD_ID, mCardId);
        outState.putParcelable(STATE_TASK, mTask);
        outState.putBoolean(STATE_RUNNING, mRunning);
        outState.putLong(STATE_REMAINING_TIME, mRemainingTime);
        outState.putLong(STATE_LAST_TICK, mLastTick);
        outState.putBoolean(STATE_SKIP_NEXT_SOUND, mSkipNextSound);
    }

    @Override
    public void onBackPressed() {
        //stop();
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pause_resume:
                pauseOrResume();
                break;
            case R.id.stop:
                stop();
                break;
        }
    }

    @Override
    public boolean handleMessage(Message message) {
        switch (message.what) {
            case WHAT_UPDATE_TIMER:
                updateTick();
                return true;
            default:
                return false;
        }
    }

    private void setupAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                0,
                new Intent(this, AlarmReceiver.class),
                0);

        if (Build.VERSION.SDK_INT >= 19) {
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + mRemainingTime, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + mRemainingTime, pendingIntent);
        }
    }

    private void pauseOrResume() {
        mRunning = !mRunning;

        if (mRunning) {
            mLastTick = SystemClock.elapsedRealtime();
            mHandler.sendEmptyMessage(WHAT_UPDATE_TIMER);
        } else {
            mHandler.removeMessages(WHAT_UPDATE_TIMER);
        }

        updateButtons();
    }

    private void stop() {
        mRunning = false;
        updateTimer();
        updateButtons();
        long duration = Task.POMODORO_DURATION - mRemainingTime;
        mTask = mTask.addTime(duration);
        getSupportLoaderManager().initLoader(TASK_UPDATE_LOADER_ID,
                null,
                new TaskUpdateLoaderCallbacks(mCardId, duration, false));
        mRemainingTime = Task.POMODORO_DURATION;
    }

    private void updateTick() {
        long newTick = SystemClock.elapsedRealtime();
        long elapsed = newTick - mLastTick;
        mRemainingTime -= elapsed;

        mLastTick = newTick;
        updateTimer();

        if (mRemainingTime <= 0L) {
            mRemainingTime = 0L;
            mRunning = false;
            mHandler.removeMessages(WHAT_UPDATE_TIMER);
            onPomodoroCompleted();
        } else if (mRunning) {
            mHandler.sendEmptyMessageDelayed(WHAT_UPDATE_TIMER, REFRESH_DELAY);
        }
    }

    private void updateTimer() {
        long ticks = mRemainingTime / 1000;
        long minutes = ticks / 60;
        long seconds = ticks % 60;

        if (minutes < 0L) {
            minutes = 0L;
        }

        if (seconds < 0L) {
            seconds = 0L;
        }

        mTime.setText(String.format(Locale.US, "%d:%02d",
                minutes, seconds));
    }

    private void updateButtons() {
        if (mRunning) {
            mPauseResume.setText(R.string.pause);
        } else {
            mPauseResume.setText(R.string.resume);
        }
    }

    private void onPomodoroCompleted() {
        if (mSkipNextSound) {
            mSkipNextSound = false;
        } else {
            SoundUtils.playNotification(this);
        }

        mTask = mTask.incPomodoros();
        getSupportLoaderManager().initLoader(TASK_UPDATE_LOADER_ID, null, new TaskUpdateLoaderCallbacks(mCardId, Task.POMODORO_DURATION, true));
        TimesUpDialogFragment.newInstance().show(getSupportFragmentManager(), null);
    }

    public void onTaskMarkedAsDone() {
        getSupportLoaderManager().initLoader(MOVE_CARD_LOADER_ID, null, new MoveCardLoaderCallbacks(mCardId,
                TaskManager.getDoneListId(this)));
        String comment = getString(R.string.task_done_comment, mTask.getPodomoros(), mTask.getPrettyTotalTime());
        getSupportLoaderManager().initLoader(ADD_COMMENT_LOADER_ID, null, new AddCommentLoaderCallbacks(mCardId, comment));
        finish();
    }

    public void onShortBreak() {
        onBreak(BreakTimerActivity.BREAK_TYPE_SHORT);
    }

    public  void onLongBreak() {
        onBreak(BreakTimerActivity.BREAK_TYPE_LONG);
    }

    private void onBreak(@BreakTimerActivity.BreakType int type) {
        Intent intent = new Intent(this, BreakTimerActivity.class);
        BreakTimerActivity.fillIntent(intent, type);
        startActivity(intent);
        finish();
    }

    public void onMoveToToDo() {
        getSupportLoaderManager().initLoader(MOVE_CARD_LOADER_ID, null, new MoveCardLoaderCallbacks(mCardId,
                TaskManager.getToDoListId(this)));
        finish();
    }

    private final class TaskUpdateLoaderCallbacks implements LoaderManager.LoaderCallbacks<Boolean> {
        private final String mCardId;
        private final long mTime;
        private final boolean mIncPomodoros;

        public TaskUpdateLoaderCallbacks(String cardId, long time, boolean incPomodoros) {
            mCardId = cardId;
            mTime = time;
            mIncPomodoros = incPomodoros;
        }

        @Override
        public Loader<Boolean> onCreateLoader(int id, Bundle args) {
            return new UpdateTasksLoader(TaskTimerActivity.this,
                    mCardId,
                    mTime,
                    mIncPomodoros);
        }

        @Override
        public void onLoadFinished(Loader<Boolean> loader, Boolean success) {
            if (!success) {
                Snackbar.make(mCoordinator, R.string.error_task_sync, Snackbar.LENGTH_LONG).show();
            }

            getSupportLoaderManager().destroyLoader(TASK_UPDATE_LOADER_ID);
        }

        @Override
        public void onLoaderReset(Loader<Boolean> loader) {
        }
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
            return new MoveCardLoader(TaskTimerActivity.this, mCardId, mListId);
        }

        @Override
        public void onLoadFinished(Loader<Boolean> loader, Boolean success) {
            getLoaderManager().destroyLoader(MOVE_CARD_LOADER_ID);

            if (!success) {
                Snackbar.make(mCoordinator, R.string.error_card_move, Snackbar.LENGTH_LONG).show();
            }
        }

        @Override
        public void onLoaderReset(Loader<Boolean> loader) {
        }
    }

    private final class AddCommentLoaderCallbacks implements LoaderManager.LoaderCallbacks<Boolean> {
        private final String mCardId;
        private final String mText;

        public AddCommentLoaderCallbacks(String cardId, String text) {
            this.mCardId = cardId;
            this.mText = text;
        }

        @Override
        public Loader<Boolean> onCreateLoader(int id, Bundle args) {
            return new AddCommentLoader(TaskTimerActivity.this, mCardId, mText);
        }

        @Override
        public void onLoadFinished(Loader<Boolean> loader, Boolean success) {
            getLoaderManager().destroyLoader(ADD_COMMENT_LOADER_ID);

            if (!success) {
                Snackbar.make(mCoordinator, R.string.error_comment_creation, Snackbar.LENGTH_LONG).show();
            }
        }

        @Override
        public void onLoaderReset(Loader<Boolean> loader) {
        }
    }
}


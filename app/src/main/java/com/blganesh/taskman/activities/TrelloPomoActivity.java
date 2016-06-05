package com.blganesh.taskman.activities;

/**
 * Created by ganeshbanda on 05/06/16.
 */
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.blganesh.taskman.R;
import com.blganesh.taskman.TaskManMainActivity;
import com.blganesh.taskman.trello.TabsPagerAdapter;

public final class TrelloPomoActivity extends TaskManMainActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tasks_activity);
        setupToolbar();

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        TabsPagerAdapter adapter = new TabsPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }
}


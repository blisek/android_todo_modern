package com.blisek.todo_list.tasks.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.blisek.todo_list.R;
import com.blisek.todo_list.new_task.activity.DetailedTaskActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TasksActivity extends AppCompatActivity {

    // region CHILD VIEWS --------------------------------------------------------------------------

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tasks_view)
    RecyclerView tasksView;

    // endregion -----------------------------------------------------------------------------------

    // region VARIABLES ----------------------------------------------------------------------------

    private RecyclerView.Adapter tasksViewAdapter;
    private RecyclerView.LayoutManager tasksViewLayoutManager;

    // endregion -----------------------------------------------------------------------------------

    // region LIFECYCLE EVENTS ---------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tasks, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_new_task:
                createNewTask();
                return true;
            case R.id.action_settings:
                changeSettings();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // endregion -----------------------------------------------------------------------------------

    // region LISTENERS ----------------------------------------------------------------------------

    @OnClick(R.id.fab)
    public void onFloatingActionButtonClick(View view)
    {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        createNewTask();
    }

    // endregion -----------------------------------------------------------------------------------

    // region PRIVATE METHODS ----------------------------------------------------------------------

    private void init() {
        tasksView.setHasFixedSize(true);

        tasksViewLayoutManager = new LinearLayoutManager(this);
        tasksView.setLayoutManager(tasksViewLayoutManager);


    }

    private void createNewTask()
    {
        Intent newTaskIntent = new Intent(this, DetailedTaskActivity.class);
        startActivity(newTaskIntent);
    }

    private void changeSettings()
    {

    }

    // endregion -----------------------------------------------------------------------------------
}

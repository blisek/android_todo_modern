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
import android.widget.AdapterView;

import com.blisek.todo_list.R;
import com.blisek.todo_list.new_task.activity.DetailedTaskActivity;
import com.blisek.todo_list.new_task.helpers.DetailedTaskActivityConstants;
import com.blisek.todo_list.persistence.model.Task;
import com.blisek.todo_list.tasks.adapters.TasksViewAdapter;
import com.blisek.todo_list.tasks.helpers.TaskComparators;
import com.blisek.todo_list.tasks.helpers.TasksActivityConstants;
import com.blisek.todo_list.tasks.observables.ObservableList;

import java.util.ArrayList;
import java.util.List;

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

//    private RecyclerView.Adapter tasksViewAdapter;
    private RecyclerView.LayoutManager tasksViewLayoutManager;
    private ObservableList<Task> tasksObservableList;
    private TasksViewAdapter tasksViewAdapter;

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TasksActivityConstants.CREATE_NEW_TASK_REQUEST:
                if(resultCode != TasksActivityConstants.RESULT_CREATED) return;
                Bundle res = data.getExtras();
                Task task = (Task)res.getSerializable(DetailedTaskActivityConstants.EXTRA_TASK_PARAM);
                tasksObservableList.addWithSorting(task, TaskComparators.endDateAscendingOrderComparator);
                break;

            case TasksActivityConstants.VIEW_TASK_REQUEST:
                if(resultCode == TasksActivityConstants.RESULT_EDITED) {
                    tasksViewAdapter.notifyDataSetChanged();
                }
                else if(resultCode == TasksActivityConstants.RESULT_DELETED) {
                    int index = getReturnedTaskId(data);
                    if(index >= 0) {
                        tasksObservableList.remove(index);
                    }
                }
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
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

    // region FACTORY METHODS ----------------------------------------------------------------------

    public View.OnClickListener makeClickListenerFor(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewExistingTask(position);
            }
        };
    }

    public View.OnLongClickListener makeLongClickListenerFor(final int position) {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                deleteExistingTask(position);
                return true;
            }
        };
    }

    // endregion

    // region PRIVATE METHODS ----------------------------------------------------------------------

    private void init() {
        tasksView.setHasFixedSize(true);

        tasksViewLayoutManager = new LinearLayoutManager(this);
        tasksView.setLayoutManager(tasksViewLayoutManager);

        List<Task> tasks = Task.getTasksOrderedAscendingByEndDate();
        tasksObservableList = new ObservableList<>(new ArrayList<>(tasks));
        tasksViewAdapter = new TasksViewAdapter(this, tasksObservableList);
        tasksView.setAdapter(tasksViewAdapter);
    }

    private void createNewTask()
    {
        Intent newTaskIntent = new Intent(this, DetailedTaskActivity.class);
        startActivityForResult(newTaskIntent, TasksActivityConstants.CREATE_NEW_TASK_REQUEST);
    }

    private void viewExistingTask(int index) {
        Intent showTaskIntent = new Intent(this, DetailedTaskActivity.class);
        Bundle extrasBundle = new Bundle();
        extrasBundle.putSerializable(
                DetailedTaskActivityConstants.EXTRA_TASK_PARAM, tasksObservableList.get(index));
        extrasBundle.putInt(DetailedTaskActivityConstants.TASK_ID_PARAM, index);
        showTaskIntent.putExtras(extrasBundle);
        startActivityForResult(showTaskIntent, TasksActivityConstants.VIEW_TASK_REQUEST);
    }

    private void deleteExistingTask(int position) {
        tasksViewAdapter.deleteTask(position);
    }

    private Task getReturnedTask(Intent retIntent) {
        Bundle extraData = retIntent.getExtras();
        if(extraData == null) return null;
        return (Task)extraData.getSerializable(DetailedTaskActivityConstants.EXTRA_TASK_PARAM);
    }

    private int getReturnedTaskId(Intent intent) {
        Bundle extraData = intent.getExtras();
        if(extraData == null) return -1;
        return extraData.getInt(DetailedTaskActivityConstants.TASK_ID_PARAM, -1);
    }

    private void changeSettings()
    {

    }

    // endregion -----------------------------------------------------------------------------------
}

package com.blisek.todo_list.tasks.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blisek.todo_list.R;
import com.blisek.todo_list.persistence.model.Task;
import com.blisek.todo_list.tasks.activity.TasksActivity;
import com.blisek.todo_list.tasks.observables.ObservableList;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by bartek on 4/30/17.
 */

public class TasksViewAdapter extends RecyclerView.Adapter<TasksViewAdapter.CustomViewHolder> {
    private TasksActivity context;
    private List<Task> tasksList;
    private ObservableList<Task> taskObservableList;

    public TasksViewAdapter(TasksActivity context, ObservableList<Task> taskObservableList) {
        this.context = context;
        this.tasksList = taskObservableList.getList();
        this.taskObservableList = taskObservableList;

        taskObservableList.getPublisher().subscribeActual(new Observer<Task>() {
            public void onSubscribe(@NonNull Disposable d) {}
            public void onError(@NonNull Throwable e) {}
            public void onComplete() {}
            @Override public void onNext(@NonNull Task task) { notifyDataSetChanged(); }
        });
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tasks_list_item, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        Task task = tasksList.get(position);

        holder.title.setText(task.title);
        Date endDate = new Date(task.endDate);
        holder.setDateDayMonthText(endDate);
        holder.setDateYearText(endDate);

        holder.itemView.setOnClickListener(context.makeClickListenerFor(position));
        holder.itemView.setOnLongClickListener(context.makeLongClickListenerFor(position));
    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }

    public void deleteTask(int position) {
        taskObservableList.remove(position);
    }


    static class CustomViewHolder extends RecyclerView.ViewHolder {
        private static final SimpleDateFormat dayMonthFormat = new SimpleDateFormat("dd.MM");
        private static final SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");

        TextView dateDayMonth;
        TextView dateYear;
        TextView title;

        public CustomViewHolder(View itemView) {
            super(itemView);

            dateDayMonth = (TextView)itemView.findViewById(R.id.task_item_date_day_month_text);
            dateYear = (TextView)itemView.findViewById(R.id.task_item_date_year_text);
            title = (TextView)itemView.findViewById(R.id.task_item_title);

        }

        private void setDateDayMonthText(Date date) {
            dateDayMonth.setText(dayMonthFormat.format(date));
        }

        private void setDateYearText(Date date) {
            dateYear.setText(yearFormat.format(date));
        }
    }
}

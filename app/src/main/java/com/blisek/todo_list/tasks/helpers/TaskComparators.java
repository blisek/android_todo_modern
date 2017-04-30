package com.blisek.todo_list.tasks.helpers;

import com.blisek.todo_list.persistence.model.Task;

import java.util.Comparator;

/**
 * Created by bartek on 4/30/17.
 */

public class TaskComparators {

    /**
     * Comparator
     */
    public static final Comparator<Task> endDateDescendingOrderComparator = new Comparator<Task>() {
        @Override
        public int compare(Task o1, Task o2) {
            return -Long.compare(o1.endDate, o2.endDate);
        }
    };

    public static final Comparator<Task> endDateAscendingOrderComparator = new Comparator<Task>() {
        @Override
        public int compare(Task o1, Task o2) {
            return Long.compare(o1.endDate, o2.endDate);
        }
    };
}

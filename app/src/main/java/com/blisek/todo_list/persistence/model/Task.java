package com.blisek.todo_list.persistence.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by bartek on 4/21/17.
 */

@Table(name = "todo_tasks")
public class Task extends Model implements Serializable {

    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_CREATION_DATE = "creation_date";
    public static final String COLUMN_END_DATE = "end_date";

    @Column(name = COLUMN_TITLE)
    public String title;

    @Column(name = COLUMN_DESCRIPTION, notNull = true)
    public String description;

    @Column(name = COLUMN_CREATION_DATE, notNull = true)
    public Long creationDate;

    @Column(name = COLUMN_END_DATE)
    public Long endDate;

    public Task() {
    }

    public Task(String title, String description, Date creationDate, Date endDate) {
        this.title = title;
        this.description = description;
        this.creationDate = creationDate.getTime();
        this.endDate = endDate.getTime();
    }

    public Task(String title, String description, Long creationDate, Long endDate) {
        this.title = title;
        this.description = description;
        this.creationDate = creationDate;
        this.endDate = endDate;
    }
}

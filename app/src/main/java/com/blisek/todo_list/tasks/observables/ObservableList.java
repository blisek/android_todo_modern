package com.blisek.todo_list.tasks.observables;

import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by bartek on 4/30/17.
 */

public final class ObservableList<T> {
    private final List<T> list;
    private final PublishSubject<T> publisher;

    public ObservableList(List<T> list) {
        this.list = list;
        this.publisher = PublishSubject.create();
    }

    public final List<T> getList() {
        return list;
    }

    public final PublishSubject<T> getPublisher() {
        return publisher;
    }

    public final void add(T t) {
        if(getList().add(t))
            getPublisher().onNext(t);
    }

    public final void add(int index, T t) {
        getList().add(index, t);
        getPublisher().onNext(t);
    }

    public final void addFirst(T t) {
        add(0, t);
    }

    public final void addWithSorting(T t, Comparator<T> tComparator) {
        List<T> list = getList();
        if(tComparator != null && list.add(t)) {
            Collections.sort(list, tComparator);
            getPublisher().onNext(t);
        }
    }

    public final T get(int index) {
        return getList().get(index);
    }

    public final T remove(int index) {
        T element = getList().remove(index);
        getPublisher().onNext(element);
        return element;
    }
}

package com.quickblox.quickblox_sdk.concurrent;

import java.util.concurrent.Future;

public class CancelableTask<T> implements Task<T> {
    private final Future<?> future;
    private Task<T> task;

    public CancelableTask(Task<T> task, Future<?> future) {
        this.task = task;
        this.future = future;
    }

    @Override
    public T performBackground() throws Exception {
        return task.performBackground();
    }

    @Override
    public void performForeground(T result) {
        task.performForeground(result);
    }

    public void cancel() {
        future.cancel(true);
        task = null;
    }

    @Override
    public void performError(Exception exception) {
        task.performError(exception);
    }
}

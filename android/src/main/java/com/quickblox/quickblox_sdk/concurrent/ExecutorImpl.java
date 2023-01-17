package com.quickblox.quickblox_sdk.concurrent;

import android.os.Handler;
import android.os.Looper;

import com.quickblox.quickblox_sdk.concurrent.exception.TaskExistException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExecutorImpl implements Executor {
    private static final int CORE_POOL_SIZE = 3;
    private static final int MAX_POOL_SIZE = 5;
    private static final int KEEP_ALIVE_TIME = 1;
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

    private final Map<String, CancelableTask<?>> tasks = new HashMap<>();
    private final ThreadPoolExecutor threadPoolExecutor;

    public ExecutorImpl() {
        threadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT,
                new LinkedBlockingQueue<>());
    }

    @Override
    public <T> void add(Task<T> task) {
        Handler mainHandler = new Handler(Looper.getMainLooper());

        threadPoolExecutor.execute(() -> {
            try {
                T result = task.performBackground();
                mainHandler.post(() -> task.performForeground(result));
            } catch (Exception exception) {
                mainHandler.post(() -> task.performError(exception));
            }
        });
    }

    @Override
    public <T> void add(Task<T> task, String taskKey) {
        Handler mainHandler = new Handler(Looper.getMainLooper());

        if (tasks.containsKey(task)) {
            mainHandler.post(() -> task.performError(new TaskExistException()));
        }

        Future<?> future = threadPoolExecutor.submit(() -> {
            try {
                T result = task.performBackground();
                mainHandler.post(() -> task.performForeground(result));
            } catch (Exception exception) {
                mainHandler.post(() -> task.performError(exception));
            }
        });

        tasks.put(taskKey, new CancelableTask<>(task, future));
    }

    @Override
    public void removeTask(String taskKey) {
        CancelableTask<?> task = tasks.remove(taskKey);
        if (task != null) {
            task.cancel();
        }
    }
}

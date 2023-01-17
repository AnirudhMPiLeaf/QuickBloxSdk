package com.quickblox.quickblox_sdk.concurrent;

public interface Executor {
    <T> void add(Task<T> task);

    <T> void add(Task<T> task, String taskKey);

    void removeTask(String taskKey);
}

package com.quickblox.quickblox_sdk.concurrent;

public interface Task<T> {
    T performBackground() throws Exception;

    void performForeground(T result);

    void performError(Exception exception);
}

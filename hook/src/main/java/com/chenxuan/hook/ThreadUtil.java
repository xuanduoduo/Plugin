package com.chenxuan.hook;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ThreadUtil {
    private static final int coreSize = Runtime.getRuntime().availableProcessors() + 1;

    private static final ExecutorService fix = Executors.newFixedThreadPool(coreSize);
    private static final ExecutorService single = Executors.newSingleThreadExecutor();
    private static final ExecutorService cache = Executors.newCachedThreadPool();
    private static final ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(coreSize);

    public static ExecutorService fixThreadPool() {
        return fix;
    }

    public static ExecutorService singleThreadPool() {
        return single;
    }

    public static ExecutorService cacheThreadPool() {
        return cache;
    }

    public static ScheduledExecutorService scheduledThreadPool() {
        return scheduled;
    }
}

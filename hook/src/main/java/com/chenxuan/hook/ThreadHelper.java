package com.chenxuan.hook;

import java.util.concurrent.ExecutorService;

public class ThreadHelper {
    private void hook() {
        ExecutorService threadPool = ThreadUtil.cacheThreadPool();
    }
}

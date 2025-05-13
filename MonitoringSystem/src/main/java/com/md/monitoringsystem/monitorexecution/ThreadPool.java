package com.md.monitoringsystem.monitorexecution;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {
    private ExecutorService executor = Executors.newFixedThreadPool(50);

    public void submit(Runnable task) {
        executor.submit(task);
    }
}

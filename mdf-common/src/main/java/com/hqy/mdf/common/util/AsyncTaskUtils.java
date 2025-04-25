package com.hqy.mdf.common.util;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 异步任务工具
 * @author hqy
 */
@Slf4j
public class AsyncTaskUtils {

    private static final String THREAD_NAME_PREFIX = "common-async-task";

    private static final int PROCESSORS = Runtime.getRuntime().availableProcessors();

    private static final int CAPACITY = 1000;

    private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(PROCESSORS, PROCESSORS * 4, 60L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(CAPACITY), new NamedThreadFactory(THREAD_NAME_PREFIX), new ThreadPoolExecutor.CallerRunsPolicy()) {
        @Override
        public void execute(Runnable command) {
            super.execute(decorate(command));
        }
    };

    private static Runnable decorate(Runnable runnable) {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return () -> {
            try {
                if (contextMap != null) {
                    MDC.setContextMap(contextMap);
                }
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }

    public static void execute(Runnable command) {
        EXECUTOR.execute(command);
    }


    public static Future<?> addTask(Runnable task) {
        return EXECUTOR.submit(task);
    }

    public static <T> Future<T> submit(Callable<T> task){
        return EXECUTOR.submit(task);
    }

    public static <T> Future<T> submit(Runnable task, T result){
        return EXECUTOR.submit(task,result);
    }


    private static class NamedThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        NamedThreadFactory(String name) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            if (name == null || name.isEmpty()) {
                name = "pool";
            }
            namePrefix = name + "-" + poolNumber.getAndIncrement() + "-thread-";
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }


}

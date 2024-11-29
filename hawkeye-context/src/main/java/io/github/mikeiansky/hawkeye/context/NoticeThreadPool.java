package io.github.mikeiansky.hawkeye.context;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author mike ian
 * @date 2024/10/11
 * @desc
 **/
@Slf4j
public class NoticeThreadPool {

    public static synchronized ThreadPoolExecutor getThreadPool() {
        return new ThreadPoolExecutor(5, 5, 60, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1024), (r, executor) -> log.error("notice rejected handle"));
    }

}

package com.polaris.lesscode.permission.constant;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadPools {
    public final static ThreadPoolExecutor POOLS = (ThreadPoolExecutor) Executors.newFixedThreadPool(200);
}

package com.polaris.lesscode.permission.service;

import java.util.concurrent.TimeUnit;

/**
 * 序号 服务
 *
 * @author roamer
 * @version v1.0
 * @date 2020-09-08 11:12
 */
public interface SeqGeneratorService {

    /**
     * 下一个序号
     *
     * @param key      存储key
     * @param loader   初始化数据的加载器
     * @param time
     * @param timeUnit
     * @return
     */
    long next(String key, Loader loader, long time, TimeUnit timeUnit);

    /**
     * 下一个序号批次
     *
     * @param key      存储key
     * @param count    个数
     * @param loader   初始化数据的加载器
     * @param time
     * @param timeUnit
     * @return {@code long[]} 序号集合
     * @throws IllegalArgumentException if  count &lt; 1 or &gt; 1000
     */
    long[] nextBatch(String key, int count, Loader loader, long time, TimeUnit timeUnit) throws IllegalArgumentException;


    /**
     * 加载器
     *
     * @author roamer
     * @version v1.0
     * @date 2020-09-08 11:12
     */
    interface Loader {

        /**
         * 加载数据
         * 当数据不存在时，用于初始化数据
         *
         * @return long 该值作为初始value
         */
        long load();
    }
}

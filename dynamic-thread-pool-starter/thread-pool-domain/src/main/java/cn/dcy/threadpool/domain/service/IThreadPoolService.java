package cn.dcy.threadpool.domain.service;

import cn.dcy.threadpool.domain.model.entity.ThreadPoolEntity;

import java.util.List;

/**
 * @author Kyle
 * @date 2024/06/18
 * @description interface for Thread Pool Registry service
 */
public interface IThreadPoolService {

    /**
     * register thread pool
     * @param threadPoolEntity
     */
    void registerThread(ThreadPoolEntity threadPoolEntity);

    /**
     * update thread pool config
     * @param threadPoolEntity
     * @return
     */
    boolean updateThreadConfig(ThreadPoolEntity threadPoolEntity);

    /**
     * query all thread pool
     * @return
     */
    List<ThreadPoolEntity> queryAllThread();

    /**
     * query thread pool by name
     * @param threadName
     * @return
     */
    ThreadPoolEntity queryThreadByName(String threadName);
}
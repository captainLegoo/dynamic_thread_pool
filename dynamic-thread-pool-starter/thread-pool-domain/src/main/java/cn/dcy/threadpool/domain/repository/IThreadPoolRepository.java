package cn.dcy.threadpool.domain.repository;

import cn.dcy.threadpool.domain.model.entity.ThreadPoolEntity;

import java.util.List;

/**
 * @author Kyle
 * @date 2024/06/18
 * @description interface for Thread Pool Repository
 */
public interface IThreadPoolRepository {

    /**
     * query thread pool by name
     * @param threadName
     * @return thread pool
     */
    ThreadPoolEntity queryThreadByName(String threadName);

    /**
     * query all thread pool
     * @return list of thread pool
     */
    List<ThreadPoolEntity> queryAllThread();

    /**
     * save thread pool config
     * @param threadPoolEntity
     */
    void saveThreadPoolConfig(ThreadPoolEntity ...threadPoolEntity);

    /**
     * update thread pool config
     * @param threadPoolEntity
     * @return true if success else false
     */
    boolean updateThreadConfig(ThreadPoolEntity threadPoolEntity);
}

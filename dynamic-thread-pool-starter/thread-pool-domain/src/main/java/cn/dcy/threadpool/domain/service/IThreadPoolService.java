package cn.dcy.threadpool.domain.service;

import cn.dcy.threadpool.domain.model.entity.ThreadPoolEntity;
import cn.dcy.threadpool.domain.model.valobj.ThreadPoolVO;

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
     * @param threadPoolName thread pool name
     * @param corePoolSize core pool size
     * @param maxPoolSize  max pool size
     * @return
     */
    boolean updateThreadConfigByName(String threadPoolName, int corePoolSize, int maxPoolSize);

    /**
     * query all thread pool
     * @return
     */
    List<ThreadPoolVO> queryAllThread();

    /**
     * query thread pool by name
     * @param threadName
     * @return
     */
    ThreadPoolEntity queryThreadByName(String threadName);
}

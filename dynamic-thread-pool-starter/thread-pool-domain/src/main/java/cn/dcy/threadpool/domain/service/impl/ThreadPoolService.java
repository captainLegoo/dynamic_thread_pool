package cn.dcy.threadpool.domain.service.impl;

import cn.dcy.threadpool.domain.adapter.repository.IThreadPoolRepository;
import cn.dcy.threadpool.domain.model.entity.ThreadPoolEntity;
import cn.dcy.threadpool.domain.service.IThreadPoolService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Kyle
 * @date 2024/06/18
 * @description Thread Pool Registry service
 */
public class ThreadPoolService implements IThreadPoolService {

    private final Logger logger = LoggerFactory.getLogger(ThreadPoolService.class);

    public final String applicationName;

    private final IThreadPoolRepository threadPoolRepository;

    private final Map<String, ThreadPoolExecutor> threadPoolExecutorMap;

    public ThreadPoolService(String applicationName, IThreadPoolRepository threadPoolRepository, Map<String, ThreadPoolExecutor> threadPoolExecutorMap) {
        this.applicationName = applicationName;
        this.threadPoolRepository = threadPoolRepository;
        this.threadPoolExecutorMap = threadPoolExecutorMap;
    }

    @Override
    public void registerThread(ThreadPoolEntity threadPoolEntity) {
        if (threadPoolEntity == null || threadPoolEntity.getThreadPoolName() == null) {
            return;
        }
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                threadPoolEntity.getCorePoolSize(),
                threadPoolEntity.getMaximumPoolSize(),
                60,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100),
                new ThreadPoolExecutor.AbortPolicy()
        );

        threadPoolExecutorMap.put(threadPoolEntity.getThreadPoolName(), executor);
        threadPoolRepository.saveThreadPoolConfig(threadPoolEntity);
    }

    @Override
    public boolean updateThreadConfigByName(ThreadPoolEntity threadPoolEntity) {
        String threadPoolName = threadPoolEntity.getThreadPoolName();
        int corePoolSize = threadPoolEntity.getCorePoolSize();
        int maxPoolSize = threadPoolEntity.getMaximumPoolSize();

        if (corePoolSize <= 0 || maxPoolSize <= 0 || corePoolSize > maxPoolSize) return false;

        if (StringUtils.isBlank(threadPoolName)) return false;

        ThreadPoolExecutor executor = threadPoolExecutorMap.get(threadPoolName);
        if (executor == null) return false;

        synchronized (executor) {
            int activeCount = executor.getActiveCount();
            if (corePoolSize < activeCount) {
                logger.error("Cannot set core pool size {} lower than active thread count {}", corePoolSize, activeCount);
                throw new IllegalArgumentException("Core pool size cannot be less than the active thread count");
            }
            executor.setMaximumPoolSize(maxPoolSize);
            executor.setCorePoolSize(corePoolSize);
        }
        logger.info("Thread pool config updated, thread pool name: {}, core pool size: {}, max pool size: {}", threadPoolName, corePoolSize, maxPoolSize);
        return true;
    }

    @Override
    public List<ThreadPoolEntity> queryAllThread() {
        List<ThreadPoolEntity> threadPoolEntities = threadPoolRepository.queryAllThread();
        return threadPoolEntities;
    }

    @Override
    public ThreadPoolEntity queryThreadByName(String threadName) {
        if (threadName == null) {
            return null;
        }
        return threadPoolRepository.queryThreadByName(threadName);
    }

    @Override
    public boolean clearThreadPoolTaskQueueByName(String threadPoolName) {
        ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorMap.get(threadPoolName);
        if (threadPoolExecutor == null) return false;

        synchronized (threadPoolExecutor) {
            threadPoolExecutor.getQueue().clear();
        }

        return true;
    }

    @Override
    public boolean shutdownThreadPoolByName(String threadPoolName) {
        ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorMap.get(threadPoolName);
        if (threadPoolExecutor == null) {
            logger.warn("Thread pool not found: {}", threadPoolName);
            return false;
        }

        try {
            threadPoolExecutor.shutdown();
            if (!threadPoolExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
                logger.warn("Thread pool termination timed out, forcing shutdown: {}", threadPoolName);
                threadPoolExecutor.shutdownNow();
            }

            if (!threadPoolExecutor.isTerminated()) {
                logger.warn("Thread pool not fully terminated: {}", threadPoolName);
                return false;
            }
        } catch (Exception e) {
            logger.error("Thread pool shutdown failed, thread pool name: {}", threadPoolName, e);
            return false;
        }

        return true;
    }
}

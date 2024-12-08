package cn.dcy.threadpool.job;

import cn.dcy.threadpool.domain.model.entity.ThreadPoolEntity;
import cn.dcy.threadpool.domain.repository.IThreadPoolRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Kyle
 * @date 2024/06/21
 * @description Scheduled Job for updating ThreadPool info
 */
@Component
public class DynamicThreadPoolScheduledJob {
    private static final Logger logger = LoggerFactory.getLogger(DynamicThreadPoolScheduledJob.class);

    private final Map<String, ThreadPoolExecutor> threadPoolExecutorMap;

    private final IThreadPoolRepository threadPoolRepository;

    public DynamicThreadPoolScheduledJob(Map<String, ThreadPoolExecutor> threadPoolExecutorMap, IThreadPoolRepository threadPoolRepository) {
        this.threadPoolExecutorMap = threadPoolExecutorMap;
        this.threadPoolRepository = threadPoolRepository;
    }

    /**
     * Scheduled job for updating ThreadPool info
     */
    @Scheduled(cron = "*/5 * * * * ?")
    public void scheduledUpdateThreadPoolEntityCache() {
        logger.info("[Schedule] Scheduled job for updating Thread Pool info");
        threadPoolExecutorMap.forEach((threadPoolName, executor) -> {
            ThreadPoolEntity threadPoolEntity = threadPoolRepository.queryThreadByName(threadPoolName);
            if (threadPoolEntity != null) {
                updateEntityCache(executor, threadPoolEntity);
                //threadPoolRepository.updateThreadConfig(threadPoolEntity);
            }
        });
    }

    /**
     * Update entity cache
     * @param executor   thread pool executor
     * @param poolEntity entity from repo
     */
    private void updateEntityCache(ThreadPoolExecutor executor, ThreadPoolEntity poolEntity) {
        poolEntity.setActiveCount(executor.getActiveCount());
        poolEntity.setCorePoolSize(executor.getCorePoolSize());
        poolEntity.setMaximumPoolSize(executor.getMaximumPoolSize());
        poolEntity.setQueueSize(executor.getQueue().size());
        poolEntity.setTaskCount(executor.getTaskCount());
        poolEntity.setCompletedTaskCount(executor.getCompletedTaskCount());
        poolEntity.setTerminated(executor.isTerminated());
    }
}

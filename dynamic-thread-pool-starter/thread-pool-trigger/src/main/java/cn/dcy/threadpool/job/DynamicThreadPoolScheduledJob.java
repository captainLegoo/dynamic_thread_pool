package cn.dcy.threadpool.job;

import cn.dcy.threadpool.domain.model.entity.ThreadPoolEntity;
import cn.dcy.threadpool.domain.service.IThreadPoolService;
import com.alibaba.fastjson2.JSON;
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

    private final IThreadPoolService threadPoolService;

    public DynamicThreadPoolScheduledJob(Map<String, ThreadPoolExecutor> threadPoolExecutorMap, IThreadPoolService threadPoolService) {
        this.threadPoolExecutorMap = threadPoolExecutorMap;
        this.threadPoolService = threadPoolService;
    }

    /**
     * Scheduled job for updating ThreadPool info
     */
    @Scheduled(cron = "*/5 * * * * ?")
    public void scheduledUpdateThreadPoolEntityCache() {
        threadPoolExecutorMap.forEach((threadPoolName, executor) -> {
            ThreadPoolEntity threadPoolEntity = threadPoolService.queryThreadByName(threadPoolName);
            if (threadPoolEntity != null) {
                updateEntityCache(executor, threadPoolEntity);
                threadPoolService.updateThreadConfig(threadPoolEntity);
            }
        });
        logger.debug("Update ThreadPoolEntity Cache: {}", JSON.toJSONString(threadPoolExecutorMap));
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

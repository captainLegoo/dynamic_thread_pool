package cn.dcy.threadpool.config;

import cn.dcy.threadpool.domain.model.entity.ThreadPoolEntity;
import cn.dcy.threadpool.domain.repository.IThreadPoolRepository;
import cn.dcy.threadpool.domain.service.impl.ThreadPoolService;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Kyle
 * @date 2024/06/20
 * @description Dynamic Thread Pool Auto Configuration
 */
@Configuration
@EnableScheduling
public class DynamicThreadPoolAutoConfig {

    @Bean("threadPoolCache")
    public Cache<String, ThreadPoolEntity> threadPoolCache(Map<String, ThreadPoolExecutor> threadPoolExecutorMap) {

        Cache<String, ThreadPoolEntity> cache = CacheBuilder.newBuilder()
                .maximumSize(10000)
                .expireAfterWrite(7, TimeUnit.DAYS)
                .recordStats()
                .build();

        threadPoolExecutorMap.forEach((threadPoolName, executor) -> {
            ThreadPoolEntity entity = new ThreadPoolEntity();
            entity.setThreadPoolName(threadPoolName);
            entity.setActiveCount(executor.getActiveCount());
            entity.setCompletedTaskCount(executor.getCompletedTaskCount());
            entity.setCorePoolSize(executor.getCorePoolSize());
            entity.setMaximumPoolSize(executor.getMaximumPoolSize());
            entity.setQueueSize(executor.getQueue().size());
            entity.setTaskCount(executor.getTaskCount());
            entity.setTerminated(executor.isTerminated());
            cache.put(threadPoolName, entity);
        });

        return cache;
    }

    @Bean
    public ThreadPoolService threadPoolService(ApplicationContext context, IThreadPoolRepository threadPoolRepository, Map<String, ThreadPoolExecutor> threadPoolExecutorMap) {
        String appName = context.getEnvironment().getProperty("spring.application.name");
        return new ThreadPoolService(appName, threadPoolRepository, threadPoolExecutorMap);
    }
}

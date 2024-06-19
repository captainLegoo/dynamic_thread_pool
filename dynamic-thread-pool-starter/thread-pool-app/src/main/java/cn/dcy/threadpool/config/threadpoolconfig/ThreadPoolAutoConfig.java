package cn.dcy.threadpool.config.threadpoolconfig;

import cn.dcy.threadpool.utils.ThreadPoolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Kyle
 * @date 2024/06/19
 * @description Auto config for thread pool
 */
@Configuration
@EnableConfigurationProperties(ThreadPoolConfigProperties.class)
@ConditionalOnProperty(prefix = "thread.pool.config", name = "enabled", havingValue = "true")
public class ThreadPoolAutoConfig {

    private final Logger logger = LoggerFactory.getLogger(ThreadPoolAutoConfig.class);

    @Bean("threadPoolExecutor")
    public Map<String, ThreadPoolExecutor> generateThreadPoolExecutor(ApplicationContext context, ThreadPoolConfigProperties threadPoolConfigProperties) {
        String poolNumStr = context.getEnvironment().getProperty("thread.pool.config.thread-pool-num");
        int threadPoolNum = Integer.parseInt(poolNumStr);
        Map<String, ThreadPoolExecutor> threadPoolExecutorMap = new HashMap<>();
        for (int i = 0; i < threadPoolNum; i++) {
            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                    threadPoolConfigProperties.getCorePoolSize(),
                    threadPoolConfigProperties.getMaxPoolSize(),
                    threadPoolConfigProperties.getKeepAliveTime(),
                    ThreadPoolUtil.strToTimeUnit(threadPoolConfigProperties.getUnit()),
                    ThreadPoolUtil.strToWorkQueue(threadPoolConfigProperties.getWorkQueue(), threadPoolConfigProperties.getWorkQueueCapacity()),
                    ThreadPoolUtil.strToRejectedExecutionHandler(threadPoolConfigProperties.getRejectedExecutionHandler())
            );

            threadPoolExecutorMap.put("threadPoolExecutor" + (i + 1), threadPoolExecutor);
            logger.info("Thread pool executor {} created", (i + 1));
        }

        return threadPoolExecutorMap;
    }
}

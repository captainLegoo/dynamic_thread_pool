package cn.dcy.threadpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Kyle
 * @date 2024/06/17
 * @description Starter and Test
 */
@SpringBootApplication
public class DynamicThreadPoolTest {
    public static void main(String[] args) {
        SpringApplication.run(DynamicThreadPoolTest.class, args);
    }

    private final Logger logger = LoggerFactory.getLogger(DynamicThreadPoolTest.class);

    private static final int TASK_LIMIT = 999999;
    private final AtomicBoolean running = new AtomicBoolean(true);

    /**
     * test
     * @param threadPoolExecutorMap
     * @return
     */
    @Bean
    public ApplicationRunner test01(Map<String, ThreadPoolExecutor> threadPoolExecutorMap) {
        return args -> {
            //while (true) {
            ThreadPoolExecutor executors01 = threadPoolExecutorMap.get("threadPoolExecutor1");
            for (int i = 0; i < TASK_LIMIT; i++) {
                if (!running.get()) break;
                submitRandomTask(executors01, "threadPoolExecutor01");
                try {
                    Thread.sleep(new Random().nextInt(50) + 1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logger.info("Shutting down...");
                running.set(false);
            }));
            //}
        };
    }

    private void submitRandomTask(ThreadPoolExecutor executor, String executorName) {
        Random random = new Random();
        int initialDelay = random.nextInt(10) + 1;
        int sleepTime = random.nextInt(10) + 1;

        executor.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(initialDelay);
                TimeUnit.SECONDS.sleep(sleepTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                logger.error("Task execution failed in executor {}", executorName, e);
            }
        });
    }
}
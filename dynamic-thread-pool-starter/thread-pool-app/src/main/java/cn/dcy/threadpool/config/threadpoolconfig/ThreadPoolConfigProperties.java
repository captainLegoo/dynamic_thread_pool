package cn.dcy.threadpool.config.threadpoolconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Kyle
 * @date 2024/06/17
 * @description Configuring ThreadPoolExecutor properties
 */
@ConfigurationProperties(prefix = "thread.pool.config", ignoreInvalidFields = true)
public class ThreadPoolConfigProperties {

    /** enabled */
    private boolean enabled = false;

    /** thread pool num */
    private int threadPoolNum = 1;

    /** core pool size */
    private int corePoolSize = 8;

    /** maximum pool size */
    private int maxPoolSize = 16;

    /** keep Alive Time */
    private long keepAliveTime = 60;

    /** unit */
    private String unit = "SECONDS";

    /**
     * work queue
     * - ArrayBlockingQueue
     * - LinkedBlockingQueue
     * - PriorityBlockingQueue
     * - SynchronousQueue
     * - LinkedTransferQueue
     * - LinkedBlockingDeque
     */
    private String workQueue = "LinkedBlockingQueue";

    private int workQueueCapacity = 100;

    /**
     * rejected execution handler
     * - AbortPolicy
     * - CallerRunsPolicy
     * - DiscardPolicy
     * - DiscardOldestPolicy
     */
    private String rejectedExecutionHandler = "AbortPolicy";

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public long getKeepAliveTime() {
        return keepAliveTime;
    }

    public String getUnit() {
        return unit;
    }

    public String getWorkQueue() {
        return workQueue;
    }

    public int getWorkQueueCapacity() {
        return workQueueCapacity;
    }

    public String getRejectedExecutionHandler() {
        return rejectedExecutionHandler;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getThreadPoolNum() {
        return threadPoolNum;
    }

    public void setThreadPoolNum(int threadPoolNum) {
        this.threadPoolNum = threadPoolNum;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public void setKeepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setWorkQueue(String workQueue) {
        this.workQueue = workQueue;
    }

    public void setWorkQueueCapacity(int workQueueCapacity) {
        this.workQueueCapacity = workQueueCapacity;
    }

    public void setRejectedExecutionHandler(String rejectedExecutionHandler) {
        this.rejectedExecutionHandler = rejectedExecutionHandler;
    }
}

package cn.dcy.threadpool.domain.model.dto;

/**
 * @author Kyle
 * @date 2024/06/24
 * @description Thread Pool DTO
 */
public class ThreadPoolDTO {
    private String threadPoolName;
    private int corePoolSize;
    private int maxPoolSize;

    public String getThreadPoolName() {
        return threadPoolName;
    }

    public void setThreadPoolName(String threadPoolName) {
        this.threadPoolName = threadPoolName;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }
}

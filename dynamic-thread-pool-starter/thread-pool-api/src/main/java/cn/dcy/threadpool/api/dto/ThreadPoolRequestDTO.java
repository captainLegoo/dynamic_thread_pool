package cn.dcy.threadpool.api.dto;

/**
 * @author Kyle
 * @date 2024/12/07
 * @description thread pool request dto
 */
public class ThreadPoolRequestDTO {
    private String threadPoolName;

    public ThreadPoolRequestDTO() {
    }

    public ThreadPoolRequestDTO(String threadPoolName) {
        this.threadPoolName = threadPoolName;
    }

    public String getThreadPoolName() {
        return threadPoolName;
    }

    public void setThreadPoolName(String threadPoolName) {
        this.threadPoolName = threadPoolName;
    }
}

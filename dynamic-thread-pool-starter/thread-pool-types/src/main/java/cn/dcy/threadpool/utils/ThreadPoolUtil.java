package cn.dcy.threadpool.utils;

import java.util.concurrent.*;

/**
 * @author Kyle
 * @date 2024/06/18
 * @description Thread pool utils
 */
public class ThreadPoolUtil {

    /**
     * Convert the string into the corresponding time unitStr
     * @param unitStr
     * @return
     */
    public static TimeUnit strToTimeUnit(String unitStr) {
        switch (unitStr) {
            case "SECONDS":
                return TimeUnit.SECONDS;
            case "MINUTES":
                return TimeUnit.MINUTES;
            case "MILLISECONDS":
                return TimeUnit.MILLISECONDS;
            case "MICROSECONDS":
                return TimeUnit.MICROSECONDS;
            case "NANOSECONDS":
                return TimeUnit.NANOSECONDS;
            case "DAYS":
                return TimeUnit.DAYS;
            case "HOURS":
                return TimeUnit.HOURS;
            default:
                return TimeUnit.SECONDS;
        }
    }

    /**
     * Convert the string into the corresponding blocking queue
     * @param workQueueStr
     * @return
     */
    public static BlockingQueue<Runnable> strToWorkQueue(String workQueueStr, int workQueueCapacity) {
        switch (workQueueStr) {
            case "LinkedBlockingQueue":
                return new LinkedBlockingQueue<>(workQueueCapacity);
            case "SynchronousQueue":
                return new SynchronousQueue<>();
            case "ArrayBlockingQueue":
                return new ArrayBlockingQueue<>(workQueueCapacity);
            case "LinkedBlockingDeque":
                return new LinkedBlockingDeque<>(workQueueCapacity);
            case "PriorityBlockingQueue":
                return new PriorityBlockingQueue<>(workQueueCapacity);
            case "LinkedTransferQueue":
                return new LinkedTransferQueue<>();
            default:
                return new LinkedBlockingQueue<>(workQueueCapacity);
        }
    }

    /**
     * Convert the string into the corresponding rejected execution handler
     * @param rejectedExecutionHandlerStr
     * @return
     */
    public static RejectedExecutionHandler strToRejectedExecutionHandler(String rejectedExecutionHandlerStr) {
        switch (rejectedExecutionHandlerStr) {
            case "CallerRunsPolicy":
                return new ThreadPoolExecutor.CallerRunsPolicy();
            case "DiscardPolicy":
                return new ThreadPoolExecutor.DiscardPolicy();
            case "DiscardOldestPolicy":
                return new ThreadPoolExecutor.DiscardOldestPolicy();
            default:
                return new ThreadPoolExecutor.AbortPolicy();
        }
    }

}

package cn.dcy.threadpool.api;

import cn.dcy.threadpool.api.dto.ThreadPoolInfoDTO;
import cn.dcy.threadpool.api.dto.ThreadPoolRequestDTO;
import cn.dcy.threadpool.api.dto.UpdateThreadPoolDTO;
import cn.dcy.threadpool.response.Response;

import java.util.List;

/**
 * @author Kyle
 * @date 2024/11/12
 * @description interface of dynamic thread pool api
 */
public interface IDynamicThreadPool {

    Response<Boolean> updateThreadPoolConfig(UpdateThreadPoolDTO updateThreadPoolDTO);

    Response<List<ThreadPoolInfoDTO>> queryAllThreadPoolConfig();

    /**
     * clear all tasks in thread pool queue
     * @param threadPoolName thread pool name
     * @return true if success or false
     */
    Response<Boolean> clearThreadPoolTaskQueueByName(String threadPoolName);

    /**
     * shutdown thread pool by name
     * @param threadPoolRequestDTO thread pool request dto
     * @return true if success or false
     */
    Response<Boolean> shutdownThreadPoolByName(ThreadPoolRequestDTO threadPoolRequestDTO);

}

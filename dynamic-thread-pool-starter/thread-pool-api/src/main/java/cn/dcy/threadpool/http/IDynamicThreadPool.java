package cn.dcy.threadpool.http;

import cn.dcy.threadpool.http.dto.ThreadPoolInfoDTO;
import cn.dcy.threadpool.http.dto.UpdateThreadPoolDTO;
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

}

package cn.dcy.threadpool.http;

import cn.dcy.threadpool.domain.model.entity.ThreadPoolDataInfo;
import cn.dcy.threadpool.http.dto.ThreadPoolInfoDTO;
import cn.dcy.threadpool.http.dto.ThreadPoolRequestDTO;
import cn.dcy.threadpool.http.dto.UpdateThreadPoolDTO;
import cn.dcy.threadpool.domain.model.entity.ThreadPoolEntity;
import cn.dcy.threadpool.domain.service.IThreadPoolService;
import cn.dcy.threadpool.response.Response;
import com.alibaba.fastjson2.JSON;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kyle
 * @date 2024/06/23
 * @description Dynamic ThreadPool visualization
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/${app.config.api-version}/dynamic-thread-pool")
public class DynamicThreadPoolController implements IDynamicThreadPool {
    private final Logger log = LoggerFactory.getLogger(DynamicThreadPoolController.class);

    @Resource
    private IThreadPoolService threadPoolService;

    @RequestMapping(value = "updateThreadPoolConfig", method = RequestMethod.POST)
    @Override
    public Response<Boolean> updateThreadPoolConfig(@RequestBody UpdateThreadPoolDTO updateThreadPoolDTO) {
        log.info("[Http] Receive thread pool config update request, thread pool name: {}, core pool size: {}, max pool size: {}", updateThreadPoolDTO.getThreadPoolName(), updateThreadPoolDTO.getCorePoolSize(), updateThreadPoolDTO.getMaxPoolSize());
        ThreadPoolEntity threadPoolEntity = new ThreadPoolEntity();
        threadPoolEntity.setThreadPoolName(updateThreadPoolDTO.getThreadPoolName());
        threadPoolEntity.setCorePoolSize(updateThreadPoolDTO.getCorePoolSize());
        threadPoolEntity.setMaximumPoolSize(updateThreadPoolDTO.getMaxPoolSize());

        boolean isUpdate = threadPoolService.updateThreadConfigByName(threadPoolEntity);
        log.info("[Http] Thread pool config update result: {}, thread pool name: {}, core pool size: {}, max pool size: {}", isUpdate, updateThreadPoolDTO.getThreadPoolName(), updateThreadPoolDTO.getCorePoolSize(), updateThreadPoolDTO.getMaxPoolSize());
        return new Response<>(isUpdate);
    }

    @RequestMapping(value = "queryAllThreadPoolConfig", method = RequestMethod.GET)
    @Override
    public Response<List<ThreadPoolInfoDTO>> queryAllThreadPoolConfig() {
        log.info("[Http] Receive thread pool config query request");
        List<ThreadPoolDataInfo> threadPoolDataInfos = threadPoolService.queryAllThread();
        List<ThreadPoolInfoDTO> threadPoolInfoDTOList = new ArrayList<>(threadPoolDataInfos.size());
        for (ThreadPoolDataInfo threadPoolDataInfo : threadPoolDataInfos) {
            ThreadPoolInfoDTO threadPoolInfoDTO = new ThreadPoolInfoDTO();
            threadPoolInfoDTO.setThreadPoolName(threadPoolDataInfo.getThreadPoolName());
            threadPoolInfoDTO.setActiveCount(threadPoolDataInfo.getActiveCount());
            threadPoolInfoDTO.setCompletedTaskCount(threadPoolDataInfo.getCompletedTaskCount());
            threadPoolInfoDTO.setCorePoolSize(threadPoolDataInfo.getCorePoolSize());
            threadPoolInfoDTO.setMaximumPoolSize(threadPoolDataInfo.getMaximumPoolSize());
            threadPoolInfoDTO.setQueueSize(threadPoolDataInfo.getQueueSize());
            threadPoolInfoDTO.setTaskCount(threadPoolDataInfo.getTaskCount());
            threadPoolInfoDTO.setTerminated(threadPoolDataInfo.isTerminated());
            threadPoolInfoDTOList.add(threadPoolInfoDTO);
        }
        log.info("[Http] Thread pool config query success");
        return new Response<>(threadPoolInfoDTOList);
    }

    @RequestMapping(value = "clearThreadPoolTaskQueueByName", method = RequestMethod.DELETE)
    @Override
    public Response<Boolean> clearThreadPoolTaskQueueByName(@RequestParam String threadPoolName) {
        log.info("[Http] Receive thread pool task queue clear request, thread pool name: {}", threadPoolName);
        if (StringUtils.isBlank(threadPoolName)) return new Response<>(false);
        boolean status = threadPoolService.clearThreadPoolTaskQueueByName(threadPoolName);
        return new Response<>(status);
    }

    @RequestMapping(value = "shutdownThreadPoolByName", method = RequestMethod.POST)
    @Override
    public Response<Boolean> shutdownThreadPoolByName(@RequestBody ThreadPoolRequestDTO threadPoolRequestDTO) {
        String threadPoolName = threadPoolRequestDTO.getThreadPoolName();
        log.info("[Http] Receive thread pool shutdown request, thread pool name: {}", threadPoolName);
        boolean status = threadPoolService.shutdownThreadPoolByName(threadPoolName);
        log.info("[Http] Thread pool shutdown result: {}", status);
        return new Response<>(status);
    }
}

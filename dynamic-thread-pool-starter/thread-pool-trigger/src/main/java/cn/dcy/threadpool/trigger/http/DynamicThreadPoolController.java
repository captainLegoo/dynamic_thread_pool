package cn.dcy.threadpool.trigger.http;

import cn.dcy.threadpool.api.IDynamicThreadPool;
import cn.dcy.threadpool.api.response.Response;
import cn.dcy.threadpool.api.dto.ThreadPoolInfoDTO;
import cn.dcy.threadpool.api.dto.ThreadPoolRequestDTO;
import cn.dcy.threadpool.api.dto.UpdateThreadPoolDTO;
import cn.dcy.threadpool.domain.model.entity.ThreadPoolEntity;
import cn.dcy.threadpool.domain.service.IThreadPoolService;
import cn.dcy.threadpool.types.enums.ResponseCode;
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
@CrossOrigin("${app.config.cross-origin}")
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
        return new Response.Builder<Boolean>()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getInfo())
                .data(isUpdate)
                .build();
    }

    @RequestMapping(value = "queryAllThreadPoolConfig", method = RequestMethod.GET)
    @Override
    public Response<List<ThreadPoolInfoDTO>> queryAllThreadPoolConfig() {
        log.info("[Http] Receive thread pool config query request");
        List<ThreadPoolEntity> threadPoolDataInfos = threadPoolService.queryAllThread();
        List<ThreadPoolInfoDTO> threadPoolInfoDTOList = new ArrayList<>(threadPoolDataInfos.size());
        for (ThreadPoolEntity threadPoolEntity : threadPoolDataInfos) {
            ThreadPoolInfoDTO threadPoolInfoDTO = new ThreadPoolInfoDTO();
            threadPoolInfoDTO.setThreadPoolName(threadPoolEntity.getThreadPoolName());
            threadPoolInfoDTO.setActiveCount(threadPoolEntity.getActiveCount());
            threadPoolInfoDTO.setCompletedTaskCount(threadPoolEntity.getCompletedTaskCount());
            threadPoolInfoDTO.setCorePoolSize(threadPoolEntity.getCorePoolSize());
            threadPoolInfoDTO.setMaximumPoolSize(threadPoolEntity.getMaximumPoolSize());
            threadPoolInfoDTO.setQueueSize(threadPoolEntity.getQueueSize());
            threadPoolInfoDTO.setTaskCount(threadPoolEntity.getTaskCount());
            threadPoolInfoDTO.setTerminated(threadPoolEntity.isTerminated());
            threadPoolInfoDTOList.add(threadPoolInfoDTO);
        }
        log.info("[Http] Thread pool config query success");
        return new Response.Builder<List<ThreadPoolInfoDTO>>()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getInfo())
                .data(threadPoolInfoDTOList)
                .build();
    }

    @RequestMapping(value = "clearThreadPoolTaskQueueByName", method = RequestMethod.DELETE)
    @Override
    public Response<Boolean> clearThreadPoolTaskQueueByName(@RequestParam String threadPoolName) {
        log.info("[Http] Receive thread pool task queue clear request, thread pool name: {}", threadPoolName);
        if (StringUtils.isBlank(threadPoolName))
            return new Response.Builder<Boolean>()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .message(ResponseCode.UN_ERROR.getInfo())
                    .build();
        boolean status = threadPoolService.clearThreadPoolTaskQueueByName(threadPoolName);
        return new Response.Builder<Boolean>()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getInfo())
                .data(status)
                .build();
    }

    @RequestMapping(value = "shutdownThreadPoolByName", method = RequestMethod.POST)
    @Override
    public Response<Boolean> shutdownThreadPoolByName(@RequestBody ThreadPoolRequestDTO threadPoolRequestDTO) {
        String threadPoolName = threadPoolRequestDTO.getThreadPoolName();
        log.info("[Http] Receive thread pool shutdown request, thread pool name: {}", threadPoolName);
        boolean status = threadPoolService.shutdownThreadPoolByName(threadPoolName);
        log.info("[Http] Thread pool shutdown result: {}", status);
        return new Response.Builder<Boolean>()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getInfo())
                .data(status)
                .build();
    }
}

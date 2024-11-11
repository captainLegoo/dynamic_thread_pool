package cn.dcy.threadpool.http;

import cn.dcy.threadpool.domain.model.entity.ThreadPoolDataInfo;
import cn.dcy.threadpool.http.dto.ThreadPoolInfoDTO;
import cn.dcy.threadpool.http.dto.UpdateThreadPoolDTO;
import cn.dcy.threadpool.domain.model.entity.ThreadPoolEntity;
import cn.dcy.threadpool.domain.service.IThreadPoolService;
import cn.dcy.threadpool.response.Response;
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
public class DynamicThreadPoolController implements IDynamicThreadPool{
    private final Logger log = LoggerFactory.getLogger(DynamicThreadPoolController.class);

    @Resource
    private IThreadPoolService threadPoolService;

    @PostMapping("/updateThreadPoolConfig")
    @Override
    public Response<Boolean> updateThreadPoolConfig(@RequestBody UpdateThreadPoolDTO updateThreadPoolDTO) {
        log.info("Receive thread pool config update request, thread pool name: {}, core pool size: {}, max pool size: {}", updateThreadPoolDTO.getThreadPoolName(), updateThreadPoolDTO.getCorePoolSize(), updateThreadPoolDTO.getMaxPoolSize());
        ThreadPoolEntity threadPoolEntity = new ThreadPoolEntity();
        threadPoolEntity.setThreadPoolName(updateThreadPoolDTO.getThreadPoolName());
        threadPoolEntity.setCorePoolSize(updateThreadPoolDTO.getCorePoolSize());
        threadPoolEntity.setMaximumPoolSize(updateThreadPoolDTO.getMaxPoolSize());

        boolean isUpdate = threadPoolService.updateThreadConfigByName(threadPoolEntity);
        log.info("Thread pool config update result: {}, thread pool name: {}, core pool size: {}, max pool size: {}", isUpdate, updateThreadPoolDTO.getThreadPoolName(), updateThreadPoolDTO.getCorePoolSize(), updateThreadPoolDTO.getMaxPoolSize());
        return new Response<>(isUpdate);
    }

    @RequestMapping(value = "queryAllThreadPoolConfig", method = RequestMethod.GET)
    @Override
    public Response<List<ThreadPoolInfoDTO>> queryAllThreadPoolConfig() {
        log.info("Receive thread pool config query request");
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
            threadPoolInfoDTOList.add(threadPoolInfoDTO);
        }
        return new Response<>(threadPoolInfoDTOList);
    }
}

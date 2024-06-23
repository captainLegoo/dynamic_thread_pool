package cn.dcy.threadpool.controller;

import cn.dcy.threadpool.domain.model.valobj.ThreadPoolVO;
import cn.dcy.threadpool.domain.service.IThreadPoolService;
import cn.dcy.threadpool.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Kyle
 * @date 2024/06/23
 * @description Dynamic ThreadPool visualization
 */
@RestController
public class DynamicThreadPoolController {
    private final Logger logger = LoggerFactory.getLogger(DynamicThreadPoolController.class);

    @Resource
    private IThreadPoolService threadPoolService;

    @PostMapping("/updateThreadPoolConfig")
    public Response<Boolean> updateThreadPoolConfig(
            @RequestParam String threadPoolName,
            @RequestParam int corePoolSize,
            @RequestParam int maxPoolSize) {
        logger.error("updateThreadPoolConfig: threadPoolName={}, corePoolSize={}, maxPoolSize={}", threadPoolName, corePoolSize, maxPoolSize);
        boolean isUpdate = threadPoolService.updateThreadConfigByName(threadPoolName, corePoolSize, maxPoolSize);
        return new Response<>(isUpdate);
    }

    @RequestMapping(value = "queryAllThreadPoolConfig", method = RequestMethod.GET)
    public Response<List<ThreadPoolVO>> queryAllThreadPoolConfig() {
        return new Response<>(threadPoolService.queryAllThread());
    }
}

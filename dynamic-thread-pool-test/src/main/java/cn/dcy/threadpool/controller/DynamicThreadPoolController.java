package cn.dcy.threadpool.controller;

import cn.dcy.threadpool.domain.model.dto.ThreadPoolDTO;
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
@CrossOrigin(origins = "*")
public class DynamicThreadPoolController {
    private final Logger logger = LoggerFactory.getLogger(DynamicThreadPoolController.class);

    @Resource
    private IThreadPoolService threadPoolService;

    @PostMapping("/updateThreadPoolConfig")
    public Response<Boolean> updateThreadPoolConfig(@RequestBody ThreadPoolDTO threadPoolDTO) {
        return new Response<>(threadPoolService.updateThreadConfigByName(threadPoolDTO));
    }

    @RequestMapping(value = "queryAllThreadPoolConfig", method = RequestMethod.GET)
    public Response<List<ThreadPoolVO>> queryAllThreadPoolConfig() {
        return new Response<>(threadPoolService.queryAllThread());
    }
}

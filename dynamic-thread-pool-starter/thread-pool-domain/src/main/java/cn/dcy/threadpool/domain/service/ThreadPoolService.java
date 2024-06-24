package cn.dcy.threadpool.domain.service;

import cn.dcy.threadpool.domain.model.dto.ThreadPoolDTO;
import cn.dcy.threadpool.domain.model.entity.ThreadPoolEntity;
import cn.dcy.threadpool.domain.model.valobj.ThreadPoolVO;
import cn.dcy.threadpool.domain.repository.IThreadPoolRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Kyle
 * @date 2024/06/18
 * @description Thread Pool Registry service
 */
public class ThreadPoolService implements IThreadPoolService {

    private final Logger logger = LoggerFactory.getLogger(ThreadPoolService.class);

    public final String applicationName;

    private final IThreadPoolRepository threadPoolRepository;

    private final Map<String, ThreadPoolExecutor> threadPoolExecutorMap;

    public ThreadPoolService(String applicationName, IThreadPoolRepository threadPoolRepository, Map<String, ThreadPoolExecutor> threadPoolExecutorMap) {
        this.applicationName = applicationName;
        this.threadPoolRepository = threadPoolRepository;
        this.threadPoolExecutorMap = threadPoolExecutorMap;
    }

    @Override
    public void registerThread(ThreadPoolEntity threadPoolEntity) {
        if (threadPoolEntity == null || threadPoolEntity.getThreadPoolName() == null) {
            return;
        }
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                threadPoolEntity.getCorePoolSize(),
                threadPoolEntity.getMaximumPoolSize(),
                60,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100),
                new ThreadPoolExecutor.AbortPolicy()
        );

        threadPoolExecutorMap.put(threadPoolEntity.getThreadPoolName(), executor);
        threadPoolRepository.saveThreadPoolConfig(threadPoolEntity);
    }

    @Override
    public boolean updateThreadConfigByName(ThreadPoolDTO threadPoolDTO) {
        String threadPoolName = threadPoolDTO.getThreadPoolName();
        int corePoolSize = threadPoolDTO.getCorePoolSize();
        int maxPoolSize = threadPoolDTO.getMaxPoolSize();

        if (corePoolSize <= 0 || maxPoolSize <= 0 || corePoolSize > maxPoolSize) return false;

        if (StringUtils.isBlank(threadPoolName)) return false;

        ThreadPoolExecutor executor = threadPoolExecutorMap.get(threadPoolName);
        if (executor == null) return false;
        executor.setCorePoolSize(corePoolSize);
        executor.setMaximumPoolSize(maxPoolSize);
        logger.info("Thread pool config updated, thread pool name: {}, core pool size: {}, max pool size: {}", threadPoolName, corePoolSize, maxPoolSize);
        return true;
    }

    @Override
    public List<ThreadPoolVO> queryAllThread() {
        List<ThreadPoolEntity> threadPoolEntities = threadPoolRepository.queryAllThread();
        List<ThreadPoolVO> threadPoolVOList = new ArrayList<>(threadPoolEntities.size());
        threadPoolEntities.forEach(threadPoolEntity -> {
            ThreadPoolVO threadPoolVO = new ThreadPoolVO();
            threadPoolVO.setThreadPoolName(threadPoolEntity.getThreadPoolName());
            threadPoolVO.setActiveCount(threadPoolEntity.getActiveCount());
            threadPoolVO.setCompletedTaskCount(threadPoolEntity.getCompletedTaskCount());
            threadPoolVO.setCorePoolSize(threadPoolEntity.getCorePoolSize());
            threadPoolVO.setMaximumPoolSize(threadPoolEntity.getMaximumPoolSize());
            threadPoolVO.setQueueSize(threadPoolEntity.getQueueSize());
            threadPoolVO.setTaskCount(threadPoolEntity.getTaskCount());
            threadPoolVO.setTerminated(threadPoolEntity.isTerminated());
            threadPoolVOList.add(threadPoolVO);
        });
        return threadPoolVOList;
    }

    @Override
    public ThreadPoolEntity queryThreadByName(String threadName) {
        if (threadName == null) {
            return null;
        }
        return threadPoolRepository.queryThreadByName(threadName);
    }
}

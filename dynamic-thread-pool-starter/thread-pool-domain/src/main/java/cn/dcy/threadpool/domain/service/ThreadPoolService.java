package cn.dcy.threadpool.domain.service;

import cn.dcy.threadpool.domain.model.entity.ThreadPoolEntity;
import cn.dcy.threadpool.domain.repository.IThreadPoolRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Kyle
 * @date 2024/06/18
 * @description Thread Pool Registry service
 */
public class ThreadPoolService implements IThreadPoolService {

    private final Logger logger = LoggerFactory.getLogger(ThreadPoolService.class);

    public final String applicationName;

    private final IThreadPoolRepository threadPoolRepository;

    public ThreadPoolService(String applicationName, IThreadPoolRepository threadPoolRepository) {
        this.applicationName = applicationName;
        this.threadPoolRepository = threadPoolRepository;
    }

    @Override
    public void registerThread(ThreadPoolEntity threadPoolEntity) {
        if (threadPoolEntity == null || threadPoolEntity.getThreadPoolName() == null) {
            return;
        }
        threadPoolRepository.saveThreadPoolConfig(threadPoolEntity);
    }

    @Override
    public boolean updateThreadConfig(ThreadPoolEntity threadPoolEntity) {
        if (threadPoolEntity == null || threadPoolEntity.getThreadPoolName() == null) {
            return false;
        }
        return threadPoolRepository.updateThreadConfig(threadPoolEntity);
    }

    @Override
    public List<ThreadPoolEntity> queryAllThread() {
        return threadPoolRepository.queryAllThread();
    }

    @Override
    public ThreadPoolEntity queryThreadByName(String threadName) {
        if (threadName == null) {
            return null;
        }
        return threadPoolRepository.queryThreadByName(threadName);
    }
}

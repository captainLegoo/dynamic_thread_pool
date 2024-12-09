package cn.dcy.threadpool.infrastructure.adapter.repository;

import cn.dcy.threadpool.domain.model.entity.ThreadPoolEntity;
import cn.dcy.threadpool.domain.repository.IThreadPoolRepository;
import com.alibaba.fastjson2.JSON;
import com.google.common.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kyle
 * @date 2024/06/18
 * @description Thread Pool Repository
 */
@Repository
public class ThreadPoolRepository implements IThreadPoolRepository {

    private final Logger logger = LoggerFactory.getLogger(ThreadPoolRepository.class);

    @Resource(name = "threadPoolCache")
    private Cache<String, ThreadPoolEntity> threadPoolCache;

    @Override
    public ThreadPoolEntity queryThreadByName(String threadName) {
        return threadPoolCache.getIfPresent(threadName);
    }

    @Override
    public List<ThreadPoolEntity> queryAllThread() {
        List<ThreadPoolEntity> list = new ArrayList<>(threadPoolCache.asMap().size());
        threadPoolCache.asMap().forEach((k, v) -> {
            list.add(v);
        });
        return list;
    }

    @Override
    public void saveThreadPoolConfig(ThreadPoolEntity ...threadPoolEntity) {
        for (ThreadPoolEntity poolEntity : threadPoolEntity) {
            threadPoolCache.put(poolEntity.getThreadPoolName(), poolEntity);
        }
    }

    @Override
    public boolean updateThreadConfig(ThreadPoolEntity threadPoolEntity) {
        ThreadPoolEntity entity =  threadPoolCache.getIfPresent(threadPoolEntity.getThreadPoolName());

        if (entity != null) {
            entity.setCorePoolSize(threadPoolEntity.getCorePoolSize());
            entity.setMaximumPoolSize(threadPoolEntity.getMaximumPoolSize());
            threadPoolCache.put(entity.getThreadPoolName(), entity);
            logger.info("ThreadPoolName:{}, updateThreadConfig: {}", entity.getThreadPoolName(), JSON.toJSONString(entity));
            return true;
        }
        return false;
    }
}

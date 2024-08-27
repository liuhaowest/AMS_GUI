package com.student.redis.delay.container;

import com.alibaba.fastjson.JSON;
import com.student.redis.delay.bean.DelayJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 延时处理队列
 * @author daify
 * @date 2019-07-26 14:41
 **/
@Slf4j
@Component
public class DelayBucket {

    @Autowired
    private RedisTemplate redisTemplate;

    private static final AtomicInteger index = new AtomicInteger(0);

    @Value("${thread.size}")
    private int bucketsSize;

    private final List <String> bucketNames = new ArrayList <>();

    @Bean
    public List <String> createBuckets() {
        for (int i = 0; i < bucketsSize; i++) {
            bucketNames.add("bucket" + i);
        }
        return bucketNames;
    }

    /**
     * 获得桶的名称
     */
    private String getThisBucketName() {
        int thisIndex = index.addAndGet(1);
        int i1 = thisIndex % bucketsSize;
        return bucketNames.get(i1);
    }

    /**
     * 获得桶集合
     */
    private BoundZSetOperations<String,Object> getBucket(String bucketName) {
        return redisTemplate.boundZSetOps(bucketName);
    }

    /**
     * 放入延时任务
     */
    public void addDelayJob(DelayJob job) {
        log.info("添加延迟任务:{}", JSON.toJSONString(job));
        String thisBucketName = getThisBucketName();
        BoundZSetOperations bucket = getBucket(thisBucketName);
        bucket.add(job,job.getDelayDate());
    }

    /**
     * 获得最新的延期任务
     */
    public DelayJob getFirstDelayTime(Integer index) {
        String name = bucketNames.get(index);
        BoundZSetOperations<String, Object> bucket = getBucket(name);
        Set<ZSetOperations.TypedTuple<Object>> set = bucket.rangeWithScores(0, 1);
        if (CollectionUtils.isEmpty(set)) {
            return null;
        }
        ZSetOperations.TypedTuple typedTuple = (ZSetOperations.TypedTuple) set.toArray()[0];
        Object value = typedTuple.getValue();
        if (value instanceof DelayJob) {
            return (DelayJob) value;
        }
        return null;
    }

    /**
     * 移除延时任务
     */
    public void removeDelayTime(Integer index,DelayJob delayJob) {
        String name = bucketNames.get(index);
        BoundZSetOperations<String, Object> bucket = getBucket(name);
        bucket.remove(delayJob);
    }

}

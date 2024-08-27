package com.student.redis.delay.container;

import com.alibaba.fastjson.JSON;
import com.student.redis.delay.bean.Job;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * 任务池
 * @author daify
 * @date 2019-07-26 14:38
 **/
@Component
@Slf4j
public class JobPool {

    @Autowired
    private RedisTemplate redisTemplate;

    private String NAME = "job.pool";

    private BoundHashOperations getPool () {
        return redisTemplate.boundHashOps(NAME);
    }

    /**
     * 添加任务
     */
    public void addJob (Job job) {
        log.info("任务池添加任务：{}", JSON.toJSONString(job));
        getPool().put(job.getId(),job);
    }

    /**
     * 获得任务
     */
    public Job getJob(Long jobId) {
        Object o = getPool().get(jobId);
        if (o instanceof Job) {
            return (Job) o;
        }
        return null;
    }

    /**
     * 移除任务
     */
    public void removeDelayJob (Long jobId) {
        log.info("任务池移除任务：{}",jobId);
        // 移除任务
        getPool().delete(jobId);
    }
}

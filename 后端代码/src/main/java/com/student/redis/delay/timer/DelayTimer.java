package com.student.redis.delay.timer;

import com.alibaba.fastjson.JSON;
import com.student.dao.entity.StClassRoom;
import com.student.dao.entity.StGradeTea;
import com.student.dao.myenum.couEnum;
import com.student.dao.service.IStClassRoomService;
import com.student.dao.service.IStGradeTeaService;
import com.student.redis.delay.bean.DelayJob;
import com.student.redis.delay.bean.Job;
import com.student.redis.delay.constants.DelayConfig;
import com.student.redis.delay.container.*;
import com.student.redis.delay.handler.DelayJobHandler;
import com.student.redis.delay.handler.TimingCouHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author daify
 * @date 2019-08-08 14:15
 **/
@Component
public class DelayTimer implements ApplicationListener <ContextRefreshedEvent> {

    @Autowired
    private DelayBucket delayBucket;
    @Autowired
    private JobPool jobPool;
    @Autowired
    private ReadyQueue readyQueue;
    @Autowired
    private IStClassRoomService classRoomService;

    @Value("${thread.size}")
    private int length;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ExecutorService executorService = new ThreadPoolExecutor(
                length-1,
                length-1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue <Runnable>());

        //为定时开课创建一个线程监听任务
        executorService.execute(new TimingCouHandler(jobPool,readyQueue,classRoomService));

        for (int i = 0; i < length-1; i++) {
            executorService.execute(
                    new DelayJobHandler(
                            delayBucket,
                            jobPool,
                            readyQueue,
                            i));
        }

    }
}

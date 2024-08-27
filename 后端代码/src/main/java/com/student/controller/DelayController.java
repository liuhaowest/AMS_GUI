package com.student.controller;

import com.alibaba.fastjson.JSON;
import com.student.redis.delay.bean.DelayJob;
import com.student.redis.delay.bean.Job;
import com.student.servie.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("delay")
public class DelayController {

    @Autowired
    private JobService jobService;

    private final static AtomicInteger index = new AtomicInteger(0);

    private final static String[] tag = new String[]{"test","web","java"};


    /**
     * 添加 测试的时候使用
     */
    @RequestMapping(value = "addTest",method = RequestMethod.POST)
    public String addDefJobTest() {
        Job request = new Job();
        int i = index.addAndGet(1);
        Long aLong = Long.valueOf(i);
        request.setId(aLong);
        int num = i%3;
        request.setTopic(tag[num]);
        request.setMessage("tag:" + tag[num] + "id:" + i);
        request.setDelayTime(10000);
        request.setTtrTime(10000);
        DelayJob delayJob = jobService.addDefJob(request);
        return JSON.toJSONString(delayJob);
    }

    /**
     * 添加
     */
    @RequestMapping(value = "add",method = RequestMethod.POST)
    public String addDefJob(@RequestBody Job request) {
        DelayJob delayJob = jobService.addDefJob(request);
        return JSON.toJSONString(delayJob);
    }

    /**
     * 获取
     */
    @RequestMapping(value = "pop/{topic}",method = RequestMethod.GET)
    public String getProcessJob(@PathVariable("topic") String topic) {
        Job process = jobService.getProcessJob(topic);
        return JSON.toJSONString(process);
    }

    /**
     * 完成一个执行的任务
     */
    @RequestMapping(value = "finish",method = RequestMethod.DELETE)
    public String finishJob(Long jobId) {
        jobService.finishJob(jobId);
        return "success";
    }

    @RequestMapping(value = "delete",method = RequestMethod.DELETE)
    public String deleteJob(Long jobId) {
        jobService.deleteJob(jobId);
        return "success";
    }

}

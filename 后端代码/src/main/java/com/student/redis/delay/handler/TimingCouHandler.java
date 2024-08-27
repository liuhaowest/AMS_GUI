package com.student.redis.delay.handler;

import com.student.dao.entity.StClassRoom;
import com.student.dao.myenum.couEnum;
import com.student.dao.service.IStClassRoomService;
import com.student.redis.delay.bean.DelayJob;
import com.student.redis.delay.bean.Job;
import com.student.redis.delay.constants.DelayConfig;
import com.student.redis.delay.container.JobPool;
import com.student.redis.delay.container.ReadyQueue;
import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * 定时开课的业务处理
 */
@Data
@AllArgsConstructor
public class TimingCouHandler implements Runnable{


    private JobPool jobPool;
    private ReadyQueue readyQueue;
    private IStClassRoomService classRoomService;


    @Override
    public void run() {
        while (true){
            DelayJob delayJob=readyQueue.popJob("test");
            if(delayJob!=null){
                Job job = jobPool.getJob(delayJob.getJodId());
                StClassRoom classRoom=new StClassRoom();
                classRoom.setId(job.getId());
                classRoom.setState(couEnum.DING);
                classRoomService.updateById(classRoom);
                jobPool.removeDelayJob(job.getId());
            }else {
                sleep();
            }
        }
    }

    private void sleep(){
        try {
            Thread.sleep(DelayConfig.SLEEP_TIME);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}

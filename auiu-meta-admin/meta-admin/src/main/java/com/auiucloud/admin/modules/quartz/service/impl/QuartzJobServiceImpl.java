package com.auiucloud.admin.modules.quartz.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.auiucloud.admin.modules.quartz.domain.QuartzJob;
import com.auiucloud.admin.modules.quartz.mapper.QuartzJobMapper;
import com.auiucloud.admin.modules.quartz.service.IQuartzJobService;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.exception.ApiException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 定时任务在线管理
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QuartzJobServiceImpl extends ServiceImpl<QuartzJobMapper, QuartzJob> implements IQuartzJobService {

    private final Scheduler scheduler;

    /**
     * 立即执行的任务分组
     */
    private static final String JOB_TEST_GROUP = "test_group";

    @Override
    public List<QuartzJob> findByJobClassName(String jobClassName) {
        return baseMapper.findByJobClassName(jobClassName);
    }

    /**
     * 保存&启动定时任务
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveAndScheduleJob(QuartzJob quartzJob) {
        // DB设置修改
        quartzJob.setDeleted(false);
        boolean success = this.save(quartzJob);
        if (success) {
            if (quartzJob.getStatus().equals(CommonConstant.STATUS_NORMAL_VALUE)) {
                // 定时器添加
                this.schedulerAdd(quartzJob.getId(), quartzJob.getJobClassName().trim(), quartzJob.getCronExpression().trim(), quartzJob.getParameter());
            }
        }
        return success;
    }

    /**
     * 恢复定时任务
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean resumeJob(QuartzJob quartzJob) {
        schedulerDelete(quartzJob.getId());
        schedulerAdd(quartzJob.getId(), quartzJob.getJobClassName().trim(), quartzJob.getCronExpression().trim(), quartzJob.getParameter());
        quartzJob.setStatus(CommonConstant.STATUS_NORMAL_VALUE);
        return this.updateById(quartzJob);
    }

    /**
     * 编辑&启停定时任务
     *
     * @throws SchedulerException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean editAndScheduleJob(QuartzJob quartzJob) throws SchedulerException {
        if (quartzJob.getStatus().equals(CommonConstant.STATUS_NORMAL_VALUE)) {
            schedulerDelete(quartzJob.getId());
            schedulerAdd(quartzJob.getId(), quartzJob.getJobClassName().trim(), quartzJob.getCronExpression().trim(), quartzJob.getParameter());
        } else {
            scheduler.pauseJob(JobKey.jobKey(quartzJob.getId()));
        }
        return this.updateById(quartzJob);
    }

    /**
     * 删除&停止删除定时任务
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAndStopJob(QuartzJob job) {
        schedulerDelete(job.getId());
		return this.removeById(job.getId());
    }

    @Override
    public void execute(QuartzJob quartzJob) throws Exception {
        String jobName = quartzJob.getJobClassName().trim();
        Date startDate = new Date();
        String ymd = DateUtil.format(startDate, DatePattern.PURE_DATETIME_PATTERN);
        String identity = jobName + ymd;
        //3秒后执行 只执行一次
        // update-begin--author:sunjianlei ---- date:20210511--- for：定时任务立即执行，延迟3秒改成0.1秒-------
        startDate.setTime(startDate.getTime() + 100L);
        // update-end--author:sunjianlei ---- date:20210511--- for：定时任务立即执行，延迟3秒改成0.1秒-------
        // 定义一个Trigger
        SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger()
                .withIdentity(identity, JOB_TEST_GROUP)
                .startAt(startDate)
                .build();
        // 构建job信息
        JobDetail jobDetail = JobBuilder.newJob(getClass(jobName).getClass()).withIdentity(identity).usingJobData("parameter", quartzJob.getParameter()).build();
        // 将trigger和 jobDetail 加入这个调度
        scheduler.scheduleJob(jobDetail, trigger);
        // 启动scheduler
        scheduler.start();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pause(QuartzJob quartzJob) {
        schedulerDelete(quartzJob.getId());
        quartzJob.setStatus(CommonConstant.STATUS_DISABLE_VALUE);
        this.updateById(quartzJob);
    }

    /**
     * 添加定时任务
     *
     * @param jobClassName
     * @param cronExpression
     * @param parameter
     */
    private void schedulerAdd(String id, String jobClassName, String cronExpression, String parameter) {
        try {
            // 启动调度器
            scheduler.start();

            // 构建job信息
            JobDetail jobDetail = JobBuilder.newJob(getClass(jobClassName).getClass()).withIdentity(id).usingJobData("parameter", parameter).build();

            // 表达式调度构建器(即任务执行的时间)
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

            // 按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(id)
                    .withSchedule(scheduleBuilder)
                    .build();

            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            throw new ApiException("创建定时任务失败", e);
        } catch (RuntimeException e) {
            throw new ApiException(e.getMessage(), e);
        } catch (Exception e) {
            throw new ApiException("后台找不到该类名：" + jobClassName, e);
        }
    }

    /**
     * 删除定时任务
     *
     * @param id
     */
    private void schedulerDelete(String id) {
        try {
            scheduler.pauseTrigger(TriggerKey.triggerKey(id));
            scheduler.unscheduleJob(TriggerKey.triggerKey(id));
            scheduler.deleteJob(JobKey.jobKey(id));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ApiException("删除定时任务失败");
        }
    }

    private static Job getClass(String classname) throws Exception {
        Class<?> class1 = Class.forName(classname);
        return (Job) class1.newInstance();
    }

}

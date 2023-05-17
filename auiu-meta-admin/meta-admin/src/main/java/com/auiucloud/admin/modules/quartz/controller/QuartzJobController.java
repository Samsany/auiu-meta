package com.auiucloud.admin.modules.quartz.controller;

import com.auiucloud.admin.modules.quartz.domain.QuartzJob;
import com.auiucloud.admin.modules.quartz.service.IQuartzJobService;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.utils.StringPool;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sys/quartzJob")
@Slf4j
public class QuartzJobController {

    private final IQuartzJobService quartzJobService;

    private final Scheduler scheduler;

    /**
     * 分页列表查询
     *
     * @param quartzJob
     * @param search
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ApiResult<?> queryPageList(Search search, QuartzJob quartzJob) {
        LambdaQueryWrapper<QuartzJob> queryWrapper = Wrappers.lambdaQuery();
        IPage<QuartzJob> pageList = quartzJobService.page(PageUtils.getPage(search), queryWrapper);
        return ApiResult.data(pageList);
    }

    /**
     * 添加定时任务
     *
     * @param quartzJob
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ApiResult<?> add(@RequestBody QuartzJob quartzJob) {
        quartzJobService.saveAndScheduleJob(quartzJob);
        return ApiResult.success("创建定时任务成功");
    }

    /**
     * 更新定时任务
     *
     * @param quartzJob
     * @return
     */
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public ApiResult<?> eidt(@RequestBody QuartzJob quartzJob) {
        try {
            quartzJobService.editAndScheduleJob(quartzJob);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            return ApiResult.fail("更新定时任务失败!");
        }
        return ApiResult.success("更新定时任务成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ApiResult<?> delete(@RequestParam(name = "id", required = true) String id) {
        QuartzJob quartzJob = quartzJobService.getById(id);
        if (quartzJob == null) {
            return ApiResult.fail("未找到对应实体");
        }
        quartzJobService.deleteAndStopJob(quartzJob);
        return ApiResult.success("删除成功!");

    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    public ApiResult<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        if (ids == null || "".equals(ids.trim())) {
            return ApiResult.fail("参数不识别！");
        }
        for (String id : Arrays.asList(ids.split(StringPool.COMMA))) {
            QuartzJob job = quartzJobService.getById(id);
            quartzJobService.deleteAndStopJob(job);
        }
        return ApiResult.success("删除定时任务成功!");
    }

    /**
     * 暂停定时任务
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/pause")
    public ApiResult<Object> pauseJob(@RequestParam(name = "id") String id) {
        QuartzJob job = quartzJobService.getById(id);
        if (job == null) {
            return ApiResult.fail("定时任务不存在！");
        }
        quartzJobService.pause(job);
        return ApiResult.success("停止定时任务成功");
    }

    /**
     * 启动定时任务
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/resume")
    public ApiResult<Object> resumeJob(@RequestParam(name = "id") String id) {
        QuartzJob job = quartzJobService.getById(id);
        if (job == null) {
            return ApiResult.fail("定时任务不存在！");
        }
        quartzJobService.resumeJob(job);
        // scheduler.resumeJob(JobKey.jobKey(job.getJobClassName().trim()));
        return ApiResult.success("启动定时任务成功");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/queryById", method = RequestMethod.GET)
    public ApiResult<?> queryById(@RequestParam(name = "id", required = true) String id) {
        QuartzJob quartzJob = quartzJobService.getById(id);
        return ApiResult.data(quartzJob);
    }

    /**
     * 立即执行
     *
     * @param id
     * @return
     */
    @GetMapping("/execute")
    public ApiResult<?> execute(@RequestParam(name = "id", required = true) String id) {
        QuartzJob quartzJob = quartzJobService.getById(id);
        if (quartzJob == null) {
            return ApiResult.fail("未找到对应实体");
        }
        try {
            quartzJobService.execute(quartzJob);
        } catch (Exception e) {
            // e.printStackTrace();
            log.info("定时任务 立即执行失败>>" + e.getMessage());
            return ApiResult.fail("执行失败!");
        }
        return ApiResult.success("执行成功!");
    }

}

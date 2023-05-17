// package com.auiucloud.admin.modules.xxjob;
//
// import com.xxl.job.core.biz.model.ReturnT;
// import com.xxl.job.core.handler.annotation.XxlJob;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.stereotype.Component;
//
///**
// * @author dries
// **/
//@Slf4j
//@Component
// public class metaJobTest {
//
//    /**
//     * 简单任务
//     *
//     * @param params
//     * @return
//     */
//
//    @XxlJob(value = "metaJobTest")
//    public ReturnT<String> demoJobHandler(String params) {
//        log.info("我是 meta-admin 服务里的定时任务 metaJobTest , 我执行了...............................");
//        return ReturnT.SUCCESS;
//    }
//
//    public void init() {
//        log.info("init");
//    }
//
//    public void destroy() {
//        log.info("destory");
//    }
//
//
//}

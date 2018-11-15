package com.rongke.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 开启定时
 * Created by bin on 2016/12/6.
 */
@Service
@Transactional
public class WorkorderCheckJob extends QuartzJobBean {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private static boolean isRun = false;

    protected void executeInternal(JobExecutionContext executionContext) throws JobExecutionException {
        if (isRun) {
            System.out.println("前一次未执行完,跳过本次任务!");
            return;
        }
        isRun = true;
        task(executionContext);
        isRun = false;
        System.out.println("执行逻辑-isRunFinish");
    }

    private void task(JobExecutionContext executionContext) {
        try {
            ApplicationContext applicationContext = (ApplicationContext) executionContext.getScheduler().getContext().get("applicationContext");

            //AppInstanceService appInstanceService = (AppInstanceService)applicationContext.getBean(AppInstanceService.class);
            //appInstanceService.clearInstanceTime();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

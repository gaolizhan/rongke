package com.rongke.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * 定时器配置类
 * Created by bin on 2016/12/6.
 */
@Configuration
public class QuartzConfiguration {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Bean
    public SchedulerFactoryBean schedulerFactory() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setTriggers(cronTriggerFactoryBean().getObject());
        schedulerFactoryBean.setTaskExecutor(threadPoolTaskExecutor());
        schedulerFactoryBean.setApplicationContextSchedulerContextKey("applicationContext");
        return schedulerFactoryBean;
    }

    @Bean
    public CronTriggerFactoryBean cronTriggerFactoryBean() {
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        cronTriggerFactoryBean.setJobDetail(jobDetailFactoryBean().getObject());
        cronTriggerFactoryBean.setCronExpression("0 0/1 * * * ?");
        //cronTriggerFactoryBean.setCronExpression("0/5 * * * * ?");
        return cronTriggerFactoryBean;
    }

    @Bean
    public JobDetailFactoryBean jobDetailFactoryBean() {
        JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
        jobDetailFactoryBean.setJobClass(WorkorderCheckJob.class);
        jobDetailFactoryBean.setDurability(true);
        return jobDetailFactoryBean;
    }

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(10);
        threadPoolTaskExecutor.setMaxPoolSize(20);
        threadPoolTaskExecutor.setQueueCapacity(5);
        threadPoolTaskExecutor.setKeepAliveSeconds(100);
        return threadPoolTaskExecutor;
    }

}

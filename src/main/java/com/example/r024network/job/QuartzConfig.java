package com.example.r024network.job;

import org.quartz.JobDetail;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

import javax.sql.DataSource;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetailFactoryBean postPublishJobDetail() {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(PostJob.class); // 绑定具体的Job类
        factoryBean.setName("postJob");
        factoryBean.setGroup("postGroup");
        factoryBean.setDurability(true);
        return factoryBean;
    }

    @Bean
    public CronTriggerFactoryBean postPublishTrigger(JobDetail postPublishJobDetail) {
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setJobDetail(postPublishJobDetail);
        factoryBean.setName("postPublishTrigger");
        factoryBean.setGroup("postGroup");
        factoryBean.setCronExpression("0 */3 * * * ?");
        return factoryBean;
    }
    /*
    "0 0/3 * * * ?"      // 每3分钟执行一次
            "0 0 9,18 * * ?"     // 每天上午9点和下午6点执行
            "0 0 12 ? * MON-FRI" // 周一至周五中午12点执行
            "0 0 0 1 * ?"        // 每月1号零点执行
     */
}
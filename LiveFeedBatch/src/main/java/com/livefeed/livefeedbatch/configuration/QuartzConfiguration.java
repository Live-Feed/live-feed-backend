package com.livefeed.livefeedbatch.configuration;

import com.livefeed.livefeedbatch.configuration.CrawlJobLauncher;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration
public class QuartzConfiguration {

    @Bean
    public JobDetail quartzJobDetail() {
        return JobBuilder.newJob(CrawlJobLauncher.class)
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger jobTrigger() {
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 0/1 * * * ?")
                .inTimeZone(TimeZone.getDefault());

        return TriggerBuilder.newTrigger()
                .forJob(quartzJobDetail())
                .withSchedule(cronScheduleBuilder)
                .build();
    }
}

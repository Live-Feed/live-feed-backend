package com.livefeed.livefeedbatch.batch.configuration;

import com.livefeed.livefeedbatch.batch.common.dto.keydto.Page;
import com.livefeed.livefeedbatch.batch.common.dto.keydto.UrlInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class CrawlJobLauncher extends QuartzJobBean {

    private final Job naverNewsCrawlJob;
    private final JobLauncher jobLauncher;


    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        Page page = Page.NAVER_SPORTS_NEWS;
        UrlInfo urlInfo = new UrlInfo(page.getService(), page.getPlatform(), page.getTheme());

        for (String pageUrl : page.getUrls()) {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("pageUrl", pageUrl)
                    .addLocalDateTime("date", LocalDateTime.now())
                    .addJobParameter("urlInfo", urlInfo, UrlInfo.class)
                    .toJobParameters();

            try {
                jobLauncher.run(naverNewsCrawlJob, jobParameters);
            } catch (Exception e) {
                log.error("batch job run error", e);
                throw new RuntimeException(e);
            }
        }
    }
}
package com.livefeed.livefeedbatch.batch.configuration;

import com.livefeed.livefeedbatch.batch.common.dto.keydto.Page;
import com.livefeed.livefeedbatch.batch.common.dto.keydto.UrlInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.autoconfigure.batch.JobLauncherApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class CrawlJobLauncher extends JobLauncherApplicationRunner {

    private final Job naverNewsCrawlJob;

    public CrawlJobLauncher(JobLauncher jobLauncher, JobExplorer jobExplorer, JobRepository jobRepository, Job naverNewsCrawlJob) {
        super(jobLauncher, jobExplorer, jobRepository);
        this.naverNewsCrawlJob = naverNewsCrawlJob;
    }

    @Override
    public void run(ApplicationArguments args) {
        Page page = Page.NAVER_SPORTS_NEWS;
        UrlInfo urlInfo = new UrlInfo(page.getService(), page.getPlatform(), page.getTheme());

        for (String pageUrl : page.getUrls()) {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("pageUrl", pageUrl)
                    .addLocalDateTime("date", LocalDateTime.now())
                    .addJobParameter("urlInfo", urlInfo, UrlInfo.class)
                    .toJobParameters();

            try {
                super.execute(naverNewsCrawlJob, jobParameters);
            } catch (Exception e) {
                log.error("batch job run error", e);
                throw new RuntimeException(e);
            }
        }
    }
}
package com.livefeed.livefeedbatch.batch.launcher;

import com.livefeed.livefeedbatch.batch.common.dto.keydto.*;
import com.livefeed.livefeedbatch.batch.domain.repository.PageRepository;
import com.livefeed.livefeedbatch.batch.domain.entity.Category;
import com.livefeed.livefeedbatch.batch.domain.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestClient;
import org.springframework.batch.core.Job;
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
    private final PageRepository pageRepository;
    private final CategoryRepository categoryRepository;
    private final RestClient elasticsearchRestClient;

    public CrawlJobLauncher(JobLauncher jobLauncher, JobExplorer jobExplorer, JobRepository jobRepository, Job naverNewsCrawlJob,
                            PageRepository pageRepository, CategoryRepository categoryRepository, RestClient elasticsearchRestClient) {
        super(jobLauncher, jobExplorer, jobRepository);
        this.naverNewsCrawlJob = naverNewsCrawlJob;
        this.pageRepository = pageRepository;
        this.categoryRepository = categoryRepository;
        this.elasticsearchRestClient = elasticsearchRestClient;
    }

    @Override
    public void run(ApplicationArguments args) {
        UrlInfo urlInfo = new UrlInfo(Service.ARTICLE, Platform.NAVER, Theme.SPORTS);
        runCrawlJob(naverNewsCrawlJob, urlInfo);
        closeElasticSearchClient();
    }

    private void runCrawlJob(Job job, UrlInfo urlInfo) {
        Category category = categoryRepository.findByServiceAndPlatformAndTheme(urlInfo.service(), urlInfo.platform(), urlInfo.theme())
                .orElseThrow(() -> new IllegalArgumentException("No category present."));

        pageRepository.findAllByCategory(category)
                .stream()
                .map(page -> new JobParametersBuilder()
                        .addString("pageUrl", page.getPageUrl())
                        .addLocalDateTime("date", LocalDateTime.now())
                        .addJobParameter("urlInfo", urlInfo, UrlInfo.class)
                        .toJobParameters())
                .forEach(jobParameters -> {
                    try {
                        super.execute(job, jobParameters);
                    } catch (Exception e) {
                        log.error("Failed to run batch job", e);
                        throw new RuntimeException(e);
                    }
                });
    }

    private void closeElasticSearchClient() {
        try {
           elasticsearchRestClient.close();
        } catch (Exception e) {
            log.error("Failed to close elasticsearch client.", e);
            throw new RuntimeException(e);
        }
    }
}
package com.livefeed.livefeedviewsbatch.batch.launcher;

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
public class ViewsUpdateJobLauncher extends JobLauncherApplicationRunner {

    private final Job job;

    public ViewsUpdateJobLauncher(JobLauncher jobLauncher, JobExplorer jobExplorer, JobRepository jobRepository, Job job) {
        super(jobLauncher, jobExplorer, jobRepository);
        this.job = job;
    }

    @Override
    public void run(ApplicationArguments args) {
        runJob(job);
    }

    private void runJob(Job job) {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLocalDateTime("date", LocalDateTime.now())
                .toJobParameters();
        try {
            super.execute(job, jobParameters);
        } catch (Exception e) {
            log.error("Failed to run value update job", e);
            throw new RuntimeException(e);
        }
    }
}
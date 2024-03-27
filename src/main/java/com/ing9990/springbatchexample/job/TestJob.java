package com.ing9990.springbatchexample.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TestJob {

    @Bean
    public Job testSimpleJob(PlatformTransactionManager txManager, JobRepository jobRepository) {
        return new JobBuilder("testSimpleJob", jobRepository)
            .start(testSimpleStep(txManager, jobRepository))
            .build();
    }

    @Bean
    public Step testSimpleStep(PlatformTransactionManager txManager, JobRepository jobRepository) {
        return new StepBuilder("testSimpleStep", jobRepository)
            .tasklet(tasklet(), txManager)
            .tasklet(tasklet2(), txManager)
            .build();
    }

    @Bean
    public Tasklet tasklet() {
        return ((contribution, chunkContext) -> {
            log.info("test");
            return RepeatStatus.FINISHED;
        });
    }

    @Bean
    public Tasklet tasklet2() {
        return (((contribution, chunkContext) -> {
            Long jobInstanceId = chunkContext
                .getStepContext()
                .getJobInstanceId();

            log.info("Job InstanceId: " + jobInstanceId);
            return RepeatStatus.FINISHED;
        }));
    }

}

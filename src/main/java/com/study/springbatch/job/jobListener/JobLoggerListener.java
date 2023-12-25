package com.study.springbatch.job.jobListener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

@Slf4j
public class JobLoggerListener implements JobExecutionListener {

    private static String BEFORE_MASSAGE = "{} Job is Running.";

    private static String AFTER_MASSAGE = "{} Job is Done. (Status: {})";

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info(BEFORE_MASSAGE, jobExecution.getJobInstance().getJobName());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info(AFTER_MASSAGE,
            jobExecution.getJobInstance().getJobName(),
            jobExecution.getStatus());

        if (jobExecution.getStatus() == BatchStatus.FAILED) {
            // 이메일 전송 작업
            log.info("Job is Fail.");
        }
    }
}

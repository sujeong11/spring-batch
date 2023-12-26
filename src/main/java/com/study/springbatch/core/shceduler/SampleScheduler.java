package com.study.springbatch.core.shceduler;

import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SampleScheduler {

    private final JobLauncher jobLauncher;

    private final Job helloWorldJob;

    @Scheduled(cron = "0 */1 * * * *") // 1분
    public void helloWorldJobRun() throws
        JobInstanceAlreadyCompleteException,
        JobExecutionAlreadyRunningException,
        JobParametersInvalidException,
        JobRestartException
    {
        JobParameters jobParameters = new JobParameters(
            Collections.singletonMap("requestTime", new JobParameter(System.currentTimeMillis()))
        );
        jobLauncher.run(helloWorldJob, jobParameters);
    }
}

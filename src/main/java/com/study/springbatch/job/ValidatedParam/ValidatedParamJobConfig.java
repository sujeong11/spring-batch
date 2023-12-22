package com.study.springbatch.job.ValidatedParam;

import com.study.springbatch.job.ValidatedParam.Validator.FileParamValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidatedParamJobConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job ValidatedParamJob(Step ValidatedParamStep) {
        return jobBuilderFactory.get("ValidatedParamJob")
                .incrementer(new RunIdIncrementer())
                .validator(new FileParamValidator())
                .start(ValidatedParamStep)
                .build();
    }

    @JobScope
    @Bean
    public Step ValidatedParamStep(Tasklet ValidatedParamTasklet) {
        return stepBuilderFactory.get("helloWorldStep")
                .tasklet(ValidatedParamTasklet)
                .build();
    }

    @StepScope
    @Bean
    public Tasklet ValidatedParamTasklet(@Value("#{jobParameters['fileName']}") String fileName) {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                System.out.println(fileName);
                System.out.println("Validated Param Tasklet");
                return RepeatStatus.FINISHED; // 작업이 끝난 이후에 무슨 작업을 할 지 반환
            }
        };
    }
}

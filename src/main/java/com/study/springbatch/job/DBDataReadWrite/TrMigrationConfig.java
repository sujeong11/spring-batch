package com.study.springbatch.job.DBDataReadWrite;

import com.study.springbatch.core.domain.accounts.Accounts;
import com.study.springbatch.core.domain.accounts.AccountsRepository;
import com.study.springbatch.core.domain.orders.Orders;
import com.study.springbatch.core.domain.orders.OrdersRepository;
import java.util.Arrays;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;

@RequiredArgsConstructor
@Configuration
public class TrMigrationConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final OrdersRepository ordersRepository;
    private final AccountsRepository accountsRepository;

    @Bean
    public Job trMigrationJob(Step trMigrationStep) {
        return jobBuilderFactory.get("trMigrationJob")
            .incrementer(new RunIdIncrementer())
            .start(trMigrationStep)
            .build();
    }

    @JobScope
    @Bean
    public Step trMigrationStep(
        ItemReader trOrdersReader,
        ItemProcessor trOrderProcessor,
        ItemWriter trOrdersWriter
    ) {
        return stepBuilderFactory.get("trMigrationStep")
            .<Orders, Accounts>chunk(5) //  한 번에 처리될 트랜잭션 단위
            .reader(trOrdersReader)
//            .writer(new ItemWriter() {
//                @Override
//                public void write(List items) throws Exception {
//                    items.forEach(System.out::println);
//                }
//            })
            .processor(trOrderProcessor)
            .writer(trOrdersWriter)
            .build();
    }

    @StepScope
    @Bean
    public RepositoryItemReader<Orders> trOrdersReader() {
        return new RepositoryItemReaderBuilder<Orders>()
            .name("trOrdersReader")
            .repository(ordersRepository)
            .methodName("findAll")
            .pageSize(5) // 한 번에 읽어올 아이템 개수
            .arguments(Arrays.asList())
            .sorts(Collections.singletonMap("id", Direction.ASC))
            .build();
    }

    @StepScope
    @Bean
    public RepositoryItemWriter<Accounts> trOrdersWriter() {
        return new RepositoryItemWriterBuilder<Accounts>()
            .repository(accountsRepository)
            .methodName("save")
            .build();
    }

    @Bean
    public ItemProcessor<Orders, Accounts> trOrderProcessor() {
        return new ItemProcessor<Orders, Accounts>() {
            @Override
            public Accounts process(Orders item) throws Exception {
                return new Accounts(item);
            }
        };
    }
}

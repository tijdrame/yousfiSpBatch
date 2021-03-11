package com.emard.batch.bankspringbatch;

import java.util.ArrayList;
import java.util.List;

import com.emard.batch.bankspringbatch.dao.BankTransaction;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@EnableBatchProcessing
//@AllArgsConstructor
public class SpringBatchConfig {
    @Autowired private JobBuilderFactory jobBuilderFactory;
    @Autowired private StepBuilderFactory stepBuilderFactory;
    @Autowired private ItemReader<BankTransaction> bankTransactionReader;
    @Autowired private ItemWriter<BankTransaction> bankTransactionWriter;
    //@Autowired private ItemProcessor<BankTransaction,BankTransaction> bankTransactionProcessor;

    @Bean
    public Job bankJobb(){
        Step step1 = stepBuilderFactory.get("step-load-data")
        .<BankTransaction, BankTransaction> chunk(100)
        .reader(bankTransactionReader)
        //.processor(bankTransactionProcessor) avt 1 seul processor
        .processor(compositeItemProcessor()) // plusieur processor
        .writer(bankTransactionWriter)
        .build();
        return jobBuilderFactory.get("bank-data-loader-job")
        .start(step1)
        .build();
    }

    @Bean
    public ItemProcessor<? super BankTransaction, ? extends BankTransaction> compositeItemProcessor() {
        List<ItemProcessor<BankTransaction, BankTransaction>> itemProcessors = new ArrayList<>();
        itemProcessors.add(ItemProcessor1());
        itemProcessors.add(ItemProcessor2());

        CompositeItemProcessor<BankTransaction, BankTransaction> composite = new CompositeItemProcessor<>();
        composite.setDelegates(itemProcessors);

        return composite;
    }

    @Bean //ce bean fait la meme chose que si on avait laissé l'annotation @compo sur la classe
    BankTransactionItemProcessor ItemProcessor1(){
        return new BankTransactionItemProcessor();
    }

    @Bean //ce bean fait la meme chose que si on avait laissé l'annotation @compo sur la classe
    BankTransactionItemAnalyticsProcessor ItemProcessor2(){
        return new BankTransactionItemAnalyticsProcessor();
    }

    @Bean
    public FlatFileItemReader<BankTransaction> flatFileItemReader(@Value("${inputFile}") Resource resource){
        FlatFileItemReader<BankTransaction> fileItemReader = new FlatFileItemReader<>();
        fileItemReader.setName("FFIR1");
        fileItemReader.setLinesToSkip(1);
        fileItemReader.setResource(resource);
        fileItemReader.setLineMapper(getLineMapper());
        return fileItemReader;
    }

    @Bean
    public LineMapper<BankTransaction> getLineMapper() {
        DefaultLineMapper<BankTransaction> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setDelimiter(",");
        delimitedLineTokenizer.setStrict(false);
        delimitedLineTokenizer.setNames("id", "accountID", "strTransactionDate", "transactionType", "amount");
        lineMapper.setLineTokenizer(delimitedLineTokenizer);
        BeanWrapperFieldSetMapper<BankTransaction> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(BankTransaction.class);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

    
}

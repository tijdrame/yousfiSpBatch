package com.emard.batch.bankspringbatch;

import java.util.List;

import com.emard.batch.bankspringbatch.dao.BankTransaction;
import com.emard.batch.bankspringbatch.dao.BankTransactionRepository;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@Component
//@AllArgsConstructor
public class BankTransactionItemWriter implements ItemWriter<BankTransaction>{

    @Autowired private BankTransactionRepository bankTransactionRepository;
    @Override
    public void write(List<? extends BankTransaction> list) throws Exception {
        bankTransactionRepository.saveAll(list);
    }
    
}

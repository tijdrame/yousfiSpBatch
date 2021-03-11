package com.emard.batch.bankspringbatch;

import com.emard.batch.bankspringbatch.dao.BankTransaction;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import lombok.Getter;
@Getter
//@Component
public class BankTransactionItemAnalyticsProcessor implements ItemProcessor<BankTransaction, BankTransaction>{

    private Double totalDebit = 0d;
    private Double totalCredit = 0d;
    @Override
    public BankTransaction process(BankTransaction bankTransaction) throws Exception {
        if(bankTransaction.getTransactionType().equals("D"))
            totalDebit += bankTransaction.getAmount();
        else totalCredit += bankTransaction.getAmount();
        return bankTransaction;
    }
    
}

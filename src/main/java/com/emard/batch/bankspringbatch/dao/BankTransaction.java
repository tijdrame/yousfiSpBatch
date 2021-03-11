package com.emard.batch.bankspringbatch.dao;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BankTransaction {
   
    @Id
    /*@SequenceGenerator(
            name = "bank_sequence",
            sequenceName = "bank_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "bank_sequence"
    )*/
    private Long id;
    private Long accountID;
    private Date transactionDate;
    private String strTransactionDate;
    @Transient
    private String transactionType;
    private Double amount;
}

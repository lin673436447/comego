package com.comego.record.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.comego.record.entity.TransactionRecord;

import java.util.List;

public interface TransactionService {

    void addTransaction(TransactionRecord transactionRecord);

    List<Long> batchAddTransactions(Long userId, List<TransactionRecord> transactionRecords);

    Page<TransactionRecord> getTransactionList(Long userId, Integer page, Integer size, String startDate, String endDate, String relationType, String contactName, Long giftBookId, Long contactId);

    TransactionRecord getTransactionDetail(Long userId, Long id);

    void updateTransaction(TransactionRecord transactionRecord);

    void deleteTransaction(Long userId, Long id);

    List<TransactionRecord> getRecentTransactions(Long userId, Integer limit);

    String exportTransactions(Long userId, String startDate, String endDate, String relationType);
}
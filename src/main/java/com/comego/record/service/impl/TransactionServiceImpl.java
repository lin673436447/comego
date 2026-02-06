package com.comego.record.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.comego.record.common.exception.BusinessException;
import com.comego.record.entity.GiftBook;
import com.comego.record.entity.TransactionRecord;
import com.comego.record.mapper.GiftBookMapper;
import com.comego.record.mapper.TransactionRecordMapper;
import com.comego.record.service.GiftBookService;
import com.comego.record.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRecordMapper transactionRecordMapper;

    @Autowired
    private GiftBookMapper giftBookMapper;

    @Autowired
    private GiftBookService giftBookService;

    @Override
    public void addTransaction(TransactionRecord transactionRecord) {
        // 如果指定了礼簿ID，查询礼簿名称并设置
        if (transactionRecord.getGiftBookId() != null) {
            GiftBook giftBook = giftBookMapper.selectById(transactionRecord.getGiftBookId());
            if (giftBook != null) {
                transactionRecord.setGiftBookName(giftBook.getBookName());
            }
        }
        transactionRecordMapper.insert(transactionRecord);
        // 插入记录后再更新礼簿统计，确保包含新记录
        if (transactionRecord.getGiftBookId() != null) {
            updateGiftBookStats(transactionRecord.getGiftBookId());
        }
    }

    @Override
    @Transactional
    public List<Long> batchAddTransactions(Long userId, List<TransactionRecord> transactionRecords) {
        List<Long> transactionIds = new ArrayList<>();

        for (TransactionRecord record : transactionRecords) {
            record.setUserId(userId);

            // 如果指定了礼簿ID，查询礼簿名称并设置
            if (record.getGiftBookId() != null) {
                GiftBook giftBook = giftBookMapper.selectById(record.getGiftBookId());
                if (giftBook != null) {
                    record.setGiftBookName(giftBook.getBookName());
                }
            }

            transactionRecordMapper.insert(record);
            transactionIds.add(record.getId());
        }

        // 批量更新礼簿统计
        for (TransactionRecord record : transactionRecords) {
            if (record.getGiftBookId() != null) {
                updateGiftBookStats(record.getGiftBookId());
            }
        }

        return transactionIds;
    }

    @Override
    public Page<TransactionRecord> getTransactionList(Long userId, Integer page, Integer size, String startDate, String endDate, String relationType, String contactName, Long giftBookId, Long contactId) {
        Page<TransactionRecord> pageParam = new Page<>(page, size);
        QueryWrapper<TransactionRecord> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("user_id", userId);
        if (startDate != null && !startDate.isEmpty()) {
            queryWrapper.ge("transaction_date", startDate);
        }
        if (endDate != null && !endDate.isEmpty()) {
            queryWrapper.le("transaction_date", endDate);
        }
        if (relationType != null && !relationType.isEmpty()) {
            queryWrapper.eq("relation_type", relationType);
        }
        if (contactName != null && !contactName.isEmpty()) {
            queryWrapper.like("contact_name", contactName);
        }
        if (giftBookId != null) {
            queryWrapper.eq("gift_book_id", giftBookId);
        }
        if (contactId != null) {
            queryWrapper.eq("contact_id", contactId);
        }

        queryWrapper.eq("is_deleted", 0);
        queryWrapper.orderByDesc("transaction_date");

        return transactionRecordMapper.selectPage(pageParam, queryWrapper);
    }

    @Override
    public TransactionRecord getTransactionDetail(Long userId, Long id) {
        QueryWrapper<TransactionRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("is_deleted", 0);
        return transactionRecordMapper.selectOne(queryWrapper);
    }

    @Override
    public void updateTransaction(TransactionRecord transactionRecord) {
        if (transactionRecord.getId() == null) {
            throw new BusinessException("记录ID不能为空");
        }
        // 验证交易是否属于该用户
        QueryWrapper<TransactionRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", transactionRecord.getId());
        queryWrapper.eq("user_id", transactionRecord.getUserId());
        queryWrapper.eq("is_deleted", 0);

        if (transactionRecordMapper.selectCount(queryWrapper) == 0) {
            throw new BusinessException("交易记录不存在或不属于该用户");
        }

        // 获取原记录，用于判断是否需要更新礼簿统计
        TransactionRecord oldRecord = transactionRecordMapper.selectById(transactionRecord.getId());

        transactionRecordMapper.updateById(transactionRecord);

        // 如果原记录关联了礼簿，更新礼簿统计
        if (oldRecord != null && oldRecord.getGiftBookId() != null) {
            updateGiftBookStats(oldRecord.getGiftBookId());
        }
        // 如果更换了礼簿（且新礼簿与原礼簿不同），也需要更新新礼簿的统计
        if (transactionRecord.getGiftBookId() != null &&
            !transactionRecord.getGiftBookId().equals(oldRecord.getGiftBookId())) {
            updateGiftBookStats(transactionRecord.getGiftBookId());
        }
    }

    @Override
    public void deleteTransaction(Long userId, Long id) {
        // 验证交易是否属于该用户
        QueryWrapper<TransactionRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("is_deleted", 0);

        if (transactionRecordMapper.selectCount(queryWrapper) == 0) {
            throw new BusinessException("交易记录不存在或不属于该用户");
        }

        // 获取记录信息，用于后续更新礼簿统计
        TransactionRecord record = transactionRecordMapper.selectById(id);

        TransactionRecord transactionRecord = new TransactionRecord();
        transactionRecord.setId(id);
        transactionRecord.setIsDeleted(1);
        transactionRecordMapper.updateById(transactionRecord);

        // 如果关联了礼簿，更新礼簿统计
        if (record != null && record.getGiftBookId() != null) {
            updateGiftBookStats(record.getGiftBookId());
        }
    }

    @Override
    public List<TransactionRecord> getRecentTransactions(Long userId, Integer limit) {
        QueryWrapper<TransactionRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("is_deleted", 0);
        queryWrapper.orderByDesc("created_at");
        queryWrapper.last("LIMIT " + limit);
        return transactionRecordMapper.selectList(queryWrapper);
    }

    @Override
    public String exportTransactions(Long userId, String startDate, String endDate, String relationType) {
        // 实际实现中，需要根据userId筛选数据
        return "https://example.com/export/transactions_20260131.xlsx";
    }

    private void updateGiftBookStats(Long giftBookId) {
        giftBookService.updateGiftBookStats(giftBookId);
    }
}

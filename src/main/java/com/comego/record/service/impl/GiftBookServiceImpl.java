package com.comego.record.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.comego.record.common.exception.BusinessException;
import com.comego.record.entity.GiftBook;
import com.comego.record.entity.TransactionRecord;
import com.comego.record.mapper.GiftBookMapper;
import com.comego.record.mapper.TransactionRecordMapper;
import com.comego.record.service.GiftBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class GiftBookServiceImpl implements GiftBookService {

    @Autowired
    private GiftBookMapper giftBookMapper;

    @Autowired
    private TransactionRecordMapper transactionRecordMapper;

    @Override
    public Page<GiftBook> getGiftBookList(Long userId, Integer page, Integer size, String bookType, String bookName) {
        Page<GiftBook> pageParam = new Page<>(page, size);
        QueryWrapper<GiftBook> queryWrapper = new QueryWrapper<>();
        
        queryWrapper.eq("user_id", userId);
        if (bookType != null && !bookType.isEmpty()) {
            queryWrapper.eq("book_type", bookType);
        }
        if (bookName != null && !bookName.isEmpty()) {
            queryWrapper.like("book_name", bookName);
        }
        
        queryWrapper.eq("is_deleted", 0);
        queryWrapper.orderByDesc("created_at");
        
        return giftBookMapper.selectPage(pageParam, queryWrapper);
    }

    @Override
    public GiftBook getGiftBookDetail(Long userId, Long id) {
        QueryWrapper<GiftBook> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("is_deleted", 0);
        return giftBookMapper.selectOne(queryWrapper);
    }

    @Override
    public void addGiftBook(GiftBook giftBook) {
        giftBook.setTotalAmount(BigDecimal.ZERO);
        giftBook.setTotalIncome(BigDecimal.ZERO);
        giftBook.setTotalExpense(BigDecimal.ZERO);
        giftBook.setIsActive(1);
        giftBook.setIsDeleted(0);
        giftBookMapper.insert(giftBook);
    }

    @Override
    public void updateGiftBook(GiftBook giftBook) {
        if (giftBook.getId() == null) {
            throw new BusinessException("礼簿ID不能为空");
        }
        QueryWrapper<GiftBook> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", giftBook.getId());
        queryWrapper.eq("user_id", giftBook.getUserId());
        queryWrapper.eq("is_deleted", 0);
        
        if (giftBookMapper.selectCount(queryWrapper) == 0) {
            throw new BusinessException("礼簿不存在或不属于该用户");
        }
        giftBookMapper.updateById(giftBook);
    }

    @Override
    public void deleteGiftBook(Long userId, Long id) {
        QueryWrapper<GiftBook> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("is_deleted", 0);
        
        if (giftBookMapper.selectCount(queryWrapper) == 0) {
            throw new BusinessException("礼簿不存在或不属于该用户");
        }
        
        GiftBook giftBook = new GiftBook();
        giftBook.setId(id);
        giftBook.setIsDeleted(1);
        giftBookMapper.updateById(giftBook);
    }

    @Override
    public List<GiftBook> getActiveGiftBooks(Long userId) {
        QueryWrapper<GiftBook> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("is_active", 1);
        queryWrapper.eq("is_deleted", 0);
        queryWrapper.orderByDesc("created_at");
        return giftBookMapper.selectList(queryWrapper);
    }

    @Override
    public void updateGiftBookStats(Long giftBookId) {
        QueryWrapper<TransactionRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("gift_book_id", giftBookId);
        queryWrapper.eq("is_deleted", 0);
        
        List<TransactionRecord> records = transactionRecordMapper.selectList(queryWrapper);
        
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;
        
        for (TransactionRecord record : records) {
            if ("income".equals(record.getTransactionDirection())) {
                if ("cash".equals(record.getTransactionMode())) {
                    totalIncome = totalIncome.add(record.getAmount());
                } else {
                    if (record.getGiftValue() != null) {
                        totalIncome = totalIncome.add(record.getGiftValue());
                    }
                }
            } else if ("expense".equals(record.getTransactionDirection())) {
                if ("cash".equals(record.getTransactionMode())) {
                    totalExpense = totalExpense.add(record.getAmount());
                } else {
                    if (record.getGiftValue() != null) {
                        totalExpense = totalExpense.add(record.getGiftValue());
                    }
                }
            }
        }
        
        GiftBook giftBook = new GiftBook();
        giftBook.setId(giftBookId);
        giftBook.setTotalIncome(totalIncome);
        giftBook.setTotalExpense(totalExpense);
        giftBook.setTotalAmount(totalIncome.subtract(totalExpense));
        giftBookMapper.updateById(giftBook);
    }

    @Override
    public List<GiftBook> getRecentGiftBooksWithRecords(Long userId, Integer limit) {
        return giftBookMapper.selectRecentGiftBooksWithRecords(userId, limit);
    }
}

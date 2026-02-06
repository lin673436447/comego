package com.comego.record.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.comego.record.entity.BalanceRecord;
import com.comego.record.mapper.BalanceRecordMapper;
import com.comego.record.service.BalanceService;
import com.comego.record.vo.BalanceReminderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BalanceServiceImpl implements BalanceService {

    @Autowired
    private BalanceRecordMapper balanceRecordMapper;

    @Override
    public com.baomidou.mybatisplus.extension.plugins.pagination.Page<BalanceRecord> getBalanceList(Long userId, Integer page, Integer size, Double minBalance, Double maxBalance) {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<BalanceRecord> pageParam = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, size);
        QueryWrapper<BalanceRecord> queryWrapper = new QueryWrapper<>();
        
        queryWrapper.eq("user_id", userId);
        if (minBalance != null) {
            queryWrapper.ge("balance", minBalance);
        }
        if (maxBalance != null) {
            queryWrapper.le("balance", maxBalance);
        }
        
        queryWrapper.eq("is_deleted", 0);
        queryWrapper.orderByDesc("created_at");
        
        return balanceRecordMapper.selectPage(pageParam, queryWrapper);
    }

    @Override
    public BalanceReminderVO getBalanceReminder(Long userId) {
        QueryWrapper<BalanceRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.ne("balance", 0);
        queryWrapper.eq("is_deleted", 0);
        queryWrapper.orderByDesc("created_at");
        
        List<BalanceRecord> records = balanceRecordMapper.selectList(queryWrapper);
        
        BalanceReminderVO vo = new BalanceReminderVO();
        List<BalanceReminderVO.BalanceRecordVO> recordVOs = new ArrayList<>();
        
        for (BalanceRecord record : records) {
            BalanceReminderVO.BalanceRecordVO recordVO = new BalanceReminderVO.BalanceRecordVO();
            recordVO.setId(record.getId());
            recordVO.setContactId(record.getContactId());
            recordVO.setContactName(record.getContactName());
            recordVO.setBalance(record.getBalance() != null ? String.format("%.2f", record.getBalance()) : "0.00");
            recordVO.setLastTransactionDate(record.getLastTransactionDate() != null ? record.getLastTransactionDate().toString() : null);
            recordVO.setLastTransactionAmount(record.getLastTransactionAmount() != null ? String.format("%.2f", record.getLastTransactionAmount()) : "0.00");
            recordVOs.add(recordVO);
        }
        
        vo.setRecords(recordVOs);
        vo.setTotalCount(recordVOs.size());
        
        return vo;
    }
}
package com.comego.record.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.comego.record.entity.BalanceRecord;
import com.comego.record.vo.BalanceReminderVO;

import java.util.List;

public interface BalanceService {

    Page<BalanceRecord> getBalanceList(Long userId, Integer page, Integer size, Double minBalance, Double maxBalance);

    BalanceReminderVO getBalanceReminder(Long userId);
}
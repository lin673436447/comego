package com.comego.record.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.comego.record.entity.BudgetSetting;
import com.comego.record.mapper.BudgetSettingMapper;
import com.comego.record.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class BudgetServiceImpl implements BudgetService {

    @Autowired
    private BudgetSettingMapper budgetSettingMapper;

    @Override
    public BudgetSetting getBudget(Long userId, String budgetType, Integer budgetYear, Integer budgetMonth, Integer budgetQuarter) {
        QueryWrapper<BudgetSetting> queryWrapper = new QueryWrapper<>();
        if (userId != null) {
            queryWrapper.eq("user_id", userId);
        }
        queryWrapper.eq("budget_type", budgetType);
        queryWrapper.eq("budget_year", budgetYear);
        if (budgetMonth != null) {
            queryWrapper.eq("budget_month", budgetMonth);
        }
        if (budgetQuarter != null) {
            queryWrapper.eq("budget_quarter", budgetQuarter);
        }
        queryWrapper.eq("is_deleted", 0);
        return budgetSettingMapper.selectOne(queryWrapper);
    }

    @Override
    public void setBudget(BudgetSetting budgetSetting) {
        BudgetSetting existing = getBudget(budgetSetting.getUserId(), budgetSetting.getBudgetType(), budgetSetting.getBudgetYear(), budgetSetting.getBudgetMonth(), budgetSetting.getBudgetQuarter());
        if (existing != null) {
            budgetSetting.setId(existing.getId());
            budgetSettingMapper.updateById(budgetSetting);
        } else {
            budgetSettingMapper.insert(budgetSetting);
        }
    }

    @Override
    public Object getBudgetStatistics(Long userId, Integer budgetYear) {
        Map<String, Object> result = new HashMap<>();
        
        BudgetSetting monthly = budgetSettingMapper.selectOne(new QueryWrapper<BudgetSetting>()
            .eq(userId != null, "user_id", userId)
            .eq("budget_type", "monthly")
            .eq("budget_year", budgetYear)
            .eq("is_deleted", 0));
        
        BudgetSetting quarterly = budgetSettingMapper.selectOne(new QueryWrapper<BudgetSetting>()
            .eq(userId != null, "user_id", userId)
            .eq("budget_type", "quarterly")
            .eq("budget_year", budgetYear)
            .eq("is_deleted", 0));
        
        BudgetSetting yearly = budgetSettingMapper.selectOne(new QueryWrapper<BudgetSetting>()
            .eq(userId != null, "user_id", userId)
            .eq("budget_type", "yearly")
            .eq("budget_year", budgetYear)
            .eq("is_deleted", 0));
        
        Map<String, Object> monthlyData = new HashMap<>();
        if (monthly != null) {
            monthlyData.put("budget", monthly.getBudgetAmount());
            monthlyData.put("used", monthly.getCurrentAmount());
            monthlyData.put("percentage", monthly.getCurrentAmount().divide(monthly.getBudgetAmount(), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")));
        }
        result.put("monthly", monthlyData);
        
        Map<String, Object> quarterlyData = new HashMap<>();
        if (quarterly != null) {
            quarterlyData.put("budget", quarterly.getBudgetAmount());
            quarterlyData.put("used", quarterly.getCurrentAmount());
            quarterlyData.put("percentage", quarterly.getCurrentAmount().divide(quarterly.getBudgetAmount(), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")));
        }
        result.put("quarterly", quarterlyData);
        
        Map<String, Object> yearlyData = new HashMap<>();
        if (yearly != null) {
            yearlyData.put("budget", yearly.getBudgetAmount());
            yearlyData.put("used", yearly.getCurrentAmount());
            yearlyData.put("percentage", yearly.getCurrentAmount().divide(yearly.getBudgetAmount(), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")));
        }
        result.put("yearly", yearlyData);
        
        return result;
    }
}
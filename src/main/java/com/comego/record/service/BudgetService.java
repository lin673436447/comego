package com.comego.record.service;

import com.comego.record.entity.BudgetSetting;

public interface BudgetService {

    BudgetSetting getBudget(Long userId, String budgetType, Integer budgetYear, Integer budgetMonth, Integer budgetQuarter);

    void setBudget(BudgetSetting budgetSetting);

    Object getBudgetStatistics(Long userId, Integer budgetYear);
}
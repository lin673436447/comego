package com.comego.record.vo;

import java.io.Serializable;

public class ExpenseVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String currentMonth;

    private String lastMonth;

    private String growthRate;

    private String yearlyTotal;

    public String getCurrentMonth() {
        return currentMonth;
    }

    public void setCurrentMonth(String currentMonth) {
        this.currentMonth = currentMonth;
    }

    public String getLastMonth() {
        return lastMonth;
    }

    public void setLastMonth(String lastMonth) {
        this.lastMonth = lastMonth;
    }

    public String getGrowthRate() {
        return growthRate;
    }

    public void setGrowthRate(String growthRate) {
        this.growthRate = growthRate;
    }

    public String getYearlyTotal() {
        return yearlyTotal;
    }

    public void setYearlyTotal(String yearlyTotal) {
        this.yearlyTotal = yearlyTotal;
    }
}
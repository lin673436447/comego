package com.comego.record.vo;

import java.io.Serializable;

public class BudgetStatisticsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private BudgetDetailVO monthly;

    private BudgetDetailVO quarterly;

    private BudgetDetailVO yearly;

    public BudgetDetailVO getMonthly() {
        return monthly;
    }

    public void setMonthly(BudgetDetailVO monthly) {
        this.monthly = monthly;
    }

    public BudgetDetailVO getQuarterly() {
        return quarterly;
    }

    public void setQuarterly(BudgetDetailVO quarterly) {
        this.quarterly = quarterly;
    }

    public BudgetDetailVO getYearly() {
        return yearly;
    }

    public void setYearly(BudgetDetailVO yearly) {
        this.yearly = yearly;
    }

    public static class BudgetDetailVO implements Serializable {
        private static final long serialVersionUID = 1L;

        private String budget;

        private String used;

        private String percentage;

        public String getBudget() {
            return budget;
        }

        public void setBudget(String budget) {
            this.budget = budget;
        }

        public String getUsed() {
            return used;
        }

        public void setUsed(String used) {
            this.used = used;
        }

        public String getPercentage() {
            return percentage;
        }

        public void setPercentage(String percentage) {
            this.percentage = percentage;
        }
    }
}
package com.comego.record.vo;

import java.io.Serializable;

public class CategoryVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String amount;

    private String percentage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
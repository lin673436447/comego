package com.comego.record.vo;

import java.io.Serializable;

public class RelationDistributionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private Integer count;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
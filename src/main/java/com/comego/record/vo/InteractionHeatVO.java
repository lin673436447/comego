package com.comego.record.vo;

import java.io.Serializable;

public class InteractionHeatVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long contactId;

    private String contactName;

    private Integer heat;

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Integer getHeat() {
        return heat;
    }

    public void setHeat(Integer heat) {
        this.heat = heat;
    }
}
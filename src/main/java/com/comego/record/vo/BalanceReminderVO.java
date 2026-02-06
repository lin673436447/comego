package com.comego.record.vo;

import java.io.Serializable;
import java.util.List;

public class BalanceReminderVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<BalanceRecordVO> records;

    private Integer totalCount;

    public List<BalanceRecordVO> getRecords() {
        return records;
    }

    public void setRecords(List<BalanceRecordVO> records) {
        this.records = records;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public static class BalanceRecordVO implements Serializable {
        private static final long serialVersionUID = 1L;

        private Long id;

        private Long contactId;

        private String contactName;

        private String balance;

        private String lastTransactionDate;

        private String lastTransactionAmount;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

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

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getLastTransactionDate() {
            return lastTransactionDate;
        }

        public void setLastTransactionDate(String lastTransactionDate) {
            this.lastTransactionDate = lastTransactionDate;
        }

        public String getLastTransactionAmount() {
            return lastTransactionAmount;
        }

        public void setLastTransactionAmount(String lastTransactionAmount) {
            this.lastTransactionAmount = lastTransactionAmount;
        }
    }
}

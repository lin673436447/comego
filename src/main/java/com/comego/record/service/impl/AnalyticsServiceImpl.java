package com.comego.record.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.comego.record.entity.ContactInfo;
import com.comego.record.entity.TransactionRecord;
import com.comego.record.mapper.ContactInfoMapper;
import com.comego.record.mapper.TransactionRecordMapper;
import com.comego.record.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    @Autowired
    private TransactionRecordMapper transactionRecordMapper;

    @Autowired
    private ContactInfoMapper contactInfoMapper;

    @Override
    public Map<String, Object> getExpense(Long userId, Integer year, Integer month) {
        QueryWrapper<TransactionRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("is_deleted", 0);

        BigDecimal currentMonthAmount = BigDecimal.ZERO;
        BigDecimal lastMonthAmount = BigDecimal.ZERO;
        BigDecimal yearlyTotal = BigDecimal.ZERO;

        List<Map<String, Object>> monthlyData = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            QueryWrapper<TransactionRecord> monthWrapper = new QueryWrapper<>();
            monthWrapper.eq("user_id", userId);
            monthWrapper.eq("is_deleted", 0);
            monthWrapper.apply("YEAR(transaction_date) = {0}", year);
            monthWrapper.apply("MONTH(transaction_date) = {0}", i);

            List<TransactionRecord> records = transactionRecordMapper.selectList(monthWrapper);
            BigDecimal monthAmount = records.stream()
                .map(TransactionRecord::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            Map<String, Object> monthData = new HashMap<>();
            monthData.put("month", i);
            monthData.put("amount", monthAmount);
            monthlyData.add(monthData);

            if (i == month) {
                currentMonthAmount = monthAmount;
            }
            if (i == month - 1) {
                lastMonthAmount = monthAmount;
            }
        }

        yearlyTotal = monthlyData.stream()
            .map(m -> (BigDecimal) m.get("amount"))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal growthRate = BigDecimal.ZERO;
        if (lastMonthAmount.compareTo(BigDecimal.ZERO) > 0) {
            growthRate = currentMonthAmount.subtract(lastMonthAmount)
                .divide(lastMonthAmount, 2, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal("100"));
        }

        Map<String, Object> result = new HashMap<>();
        result.put("currentMonth", currentMonthAmount);
        result.put("lastMonth", lastMonthAmount);
        result.put("growthRate", growthRate);
        result.put("yearlyTotal", yearlyTotal);
        result.put("monthlyData", monthlyData);

        return result;
    }

    @Override
    public Map<String, Object> getCategory(Long userId, String startDate, String endDate) {
        QueryWrapper<TransactionRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("is_deleted", 0);

        if (startDate != null && !startDate.isEmpty()) {
            queryWrapper.ge("transaction_date", startDate);
        }
        if (endDate != null && !endDate.isEmpty()) {
            queryWrapper.le("transaction_date", endDate);
        }

        List<TransactionRecord> records = transactionRecordMapper.selectList(queryWrapper);

        Map<String, BigDecimal> categoryAmountMap = new HashMap<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (TransactionRecord record : records) {
            String relationType = record.getRelationType();
            BigDecimal amount = record.getAmount();

            if (amount != null && relationType != null) {
                categoryAmountMap.put(relationType, categoryAmountMap.getOrDefault(relationType, BigDecimal.ZERO).add(amount));
                totalAmount = totalAmount.add(amount);
            }
        }

        List<Map<String, Object>> categories = new ArrayList<>();
        for (Map.Entry<String, BigDecimal> entry : categoryAmountMap.entrySet()) {
            Map<String, Object> category = new HashMap<>();
            category.put("name", entry.getKey());
            category.put("amount", entry.getValue());

            BigDecimal percentage = BigDecimal.ZERO;
            if (totalAmount.compareTo(BigDecimal.ZERO) > 0) {
                percentage = entry.getValue().divide(totalAmount, 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal("100"));
            }
            category.put("percentage", percentage);

            categories.add(category);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("categories", categories);

        return result;
    }

    @Override
    public Map<String, Object> getRelation(Long userId, Integer limit) {
        // 查询所有交易记录（不分页，确保统计准确）
        QueryWrapper<TransactionRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("is_deleted", 0);
        queryWrapper.isNotNull("contact_id");

        List<TransactionRecord> records = transactionRecordMapper.selectList(queryWrapper);

        Map<String, Integer> relationCountMap = new HashMap<>();
        Map<Long, Integer> contactTransactionCountMap = new HashMap<>();

        for (TransactionRecord record : records) {
            String relationType = record.getRelationType();
            if (relationType != null && !relationType.isEmpty()) {
                relationCountMap.put(relationType, relationCountMap.getOrDefault(relationType, 0) + 1);
            }

            Long contactId = record.getContactId();
            if (contactId != null) {
                contactTransactionCountMap.put(contactId, contactTransactionCountMap.getOrDefault(contactId, 0) + 1);
            }
        }

        List<Map<String, Object>> relationDistribution = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : relationCountMap.entrySet()) {
            Map<String, Object> relation = new HashMap<>();
            relation.put("name", entry.getKey());
            relation.put("count", entry.getValue());
            relationDistribution.add(relation);
        }

        // 账目次数排行
        List<Map<String, Object>> transactionCountRank = new ArrayList<>();
        List<Map.Entry<Long, Integer>> sortedEntries = new ArrayList<>(contactTransactionCountMap.entrySet());
        sortedEntries.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        Integer maxCount = sortedEntries.isEmpty() ? 1 : sortedEntries.get(0).getValue();

        for (int i = 0; i < Math.min(limit, sortedEntries.size()); i++) {
            Map.Entry<Long, Integer> entry = sortedEntries.get(i);
            Long contactId = entry.getKey();

            // 查询联系人姓名
            ContactInfo contact = contactInfoMapper.selectById(contactId);
            String contactName = contact != null ? contact.getName() : "未知";

            Map<String, Object> rank = new HashMap<>();
            rank.put("contactId", contactId);
            rank.put("contactName", contactName);
            rank.put("transactionCount", entry.getValue());
            rank.put("percentage", entry.getValue() * 100 / maxCount);
            transactionCountRank.add(rank);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("relationDistribution", relationDistribution);
        result.put("transactionCountRank", transactionCountRank);

        return result;
    }

    @Override
    public List<Map<String, Object>> getContactTransactions(Long userId, String startDate, String endDate) {
        QueryWrapper<TransactionRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("is_deleted", 0);

        if (startDate != null && !startDate.isEmpty()) {
            queryWrapper.ge("transaction_date", startDate);
        }
        if (endDate != null && !endDate.isEmpty()) {
            queryWrapper.le("transaction_date", endDate);
        }

        List<TransactionRecord> records = transactionRecordMapper.selectList(queryWrapper);

        Map<Long, ContactTransactionStats> contactStatsMap = new HashMap<>();

        for (TransactionRecord record : records) {
            Long contactId = record.getContactId();
            if (contactId == null) continue;

            ContactTransactionStats stats = contactStatsMap.computeIfAbsent(contactId, k -> {
                ContactTransactionStats s = new ContactTransactionStats();
                s.setContactId(contactId);
                return s;
            });

            String direction = record.getTransactionDirection();
            BigDecimal amount = record.getAmount() != null ? record.getAmount() : BigDecimal.ZERO;

            if ("income".equals(direction)) {
                stats.setTotalIncome(stats.getTotalIncome().add(amount));
            } else if ("expense".equals(direction)) {
                stats.setTotalExpense(stats.getTotalExpense().add(amount));
            }
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (ContactTransactionStats stats : contactStatsMap.values()) {
            ContactInfo contact = contactInfoMapper.selectById(stats.getContactId());
            if (contact == null) continue;

            Map<String, Object> item = new HashMap<>();
            item.put("contactId", stats.getContactId());
            item.put("contactName", contact.getName());
            item.put("avatarUrl", contact.getAvatarUrl());
            item.put("relationType", contact.getRelationType());
            item.put("totalIncome", stats.getTotalIncome());
            item.put("totalExpense", stats.getTotalExpense());
            item.put("balance", stats.getTotalIncome().subtract(stats.getTotalExpense()));

            result.add(item);
        }

        result.sort((a, b) -> {
            BigDecimal balanceA = (BigDecimal) a.get("balance");
            BigDecimal balanceB = (BigDecimal) b.get("balance");
            return balanceB.compareTo(balanceA);
        });

        return result;
    }

    private static class ContactTransactionStats {
        private Long contactId;
        private BigDecimal totalIncome = BigDecimal.ZERO;
        private BigDecimal totalExpense = BigDecimal.ZERO;

        public Long getContactId() {
            return contactId;
        }

        public void setContactId(Long contactId) {
            this.contactId = contactId;
        }

        public BigDecimal getTotalIncome() {
            return totalIncome;
        }

        public void setTotalIncome(BigDecimal totalIncome) {
            this.totalIncome = totalIncome;
        }

        public BigDecimal getTotalExpense() {
            return totalExpense;
        }

        public void setTotalExpense(BigDecimal totalExpense) {
            this.totalExpense = totalExpense;
        }
    }
}

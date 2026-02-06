package com.comego.record.service;

import java.util.List;
import java.util.Map;

public interface AnalyticsService {

    Map<String, Object> getExpense(Long userId, Integer year, Integer month);

    Map<String, Object> getCategory(Long userId, String startDate, String endDate);

    Map<String, Object> getRelation(Long userId, Integer limit);

    List<Map<String, Object>> getContactTransactions(Long userId, String startDate, String endDate);
}
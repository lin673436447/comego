package com.comego.record.controller;

import com.comego.record.common.result.Result;
import com.comego.record.common.util.TokenUtil;
import com.comego.record.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @PostMapping("/expense")
    public Result<Map<String, Object>> getExpense(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> params) {
        Long userId = TokenUtil.getUserIdFromToken(token);
        Integer year = params.get("year") != null ? Integer.parseInt(params.get("year").toString()) : null;
        Integer month = params.get("month") != null ? Integer.parseInt(params.get("month").toString()) : null;
        
        Map<String, Object> expenseData = analyticsService.getExpense(userId, year, month);
        return Result.success(expenseData);
    }

    @PostMapping("/category")
    public Result<Map<String, Object>> getCategory(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> params) {
        Long userId = TokenUtil.getUserIdFromToken(token);
        String startDate = params.get("startDate") != null ? params.get("startDate").toString() : null;
        String endDate = params.get("endDate") != null ? params.get("endDate").toString() : null;
        
        Map<String, Object> categoryData = analyticsService.getCategory(userId, startDate, endDate);
        return Result.success(categoryData);
    }

    @PostMapping("/relation")
    public Result<Map<String, Object>> getRelation(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> params) {
        Long userId = TokenUtil.getUserIdFromToken(token);
        Integer limit = params.get("limit") != null ? Integer.parseInt(params.get("limit").toString()) : 5;
        
        Map<String, Object> relationData = analyticsService.getRelation(userId, limit);
        return Result.success(relationData);
    }

    @PostMapping("/contact-transactions")
    public Result<List<Map<String, Object>>> getContactTransactions(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> params) {
        Long userId = TokenUtil.getUserIdFromToken(token);
        String startDate = params.get("startDate") != null ? params.get("startDate").toString() : null;
        String endDate = params.get("endDate") != null ? params.get("endDate").toString() : null;
        
        List<Map<String, Object>> contactTransactions = analyticsService.getContactTransactions(userId, startDate, endDate);
        return Result.success(contactTransactions);
    }
}
package com.comego.record.controller;

import com.comego.record.common.result.Result;
import com.comego.record.common.util.TokenUtil;
import com.comego.record.entity.BudgetSetting;
import com.comego.record.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/budget")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @PostMapping("/get")
    public Result<BudgetSetting> getBudget(@RequestBody Map<String, Object> params, @RequestHeader(value = "Authorization", required = false) String authorization) {
        String budgetType = params.get("budgetType") != null ? params.get("budgetType").toString() : null;
        Integer budgetYear = params.get("budgetYear") != null ? Integer.parseInt(params.get("budgetYear").toString()) : null;
        Integer budgetMonth = params.get("budgetMonth") != null ? Integer.parseInt(params.get("budgetMonth").toString()) : null;
        Integer budgetQuarter = params.get("budgetQuarter") != null ? Integer.parseInt(params.get("budgetQuarter").toString()) : null;
        
        // 从token中获取用户ID
        Long userId = null;
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            userId = TokenUtil.getUserIdFromToken(token);
        }
        
        BudgetSetting budgetSetting = budgetService.getBudget(userId, budgetType, budgetYear, budgetMonth, budgetQuarter);
        return Result.success(budgetSetting);
    }

    @PostMapping("/set")
    public Result<Map<String, Long>> setBudget(@RequestBody BudgetSetting budgetSetting, @RequestHeader(value = "Authorization", required = false) String authorization) {
        // 从token中获取用户ID
        Long userId = null;
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            userId = TokenUtil.getUserIdFromToken(token);
        }
        
        budgetSetting.setUserId(userId);
        budgetService.setBudget(budgetSetting);
        Map<String, Long> result = new HashMap<>();
        result.put("budget_id", budgetSetting.getId());
        return Result.success(result);
    }

    @PostMapping("/statistics")
    public Result<Object> getBudgetStatistics(@RequestBody Map<String, Object> params, @RequestHeader(value = "Authorization", required = false) String authorization) {
        Integer budgetYear = params.get("budgetYear") != null ? Integer.parseInt(params.get("budgetYear").toString()) : null;
        
        // 从token中获取用户ID
        Long userId = null;
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            userId = TokenUtil.getUserIdFromToken(token);
        }
        
        Object statistics = budgetService.getBudgetStatistics(userId, budgetYear);
        return Result.success(statistics);
    }
}
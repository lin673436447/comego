package com.comego.record.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.comego.record.common.result.PageResult;
import com.comego.record.common.result.Result;
import com.comego.record.common.util.TokenUtil;
import com.comego.record.entity.BalanceRecord;
import com.comego.record.service.BalanceService;
import com.comego.record.vo.BalanceReminderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/balance")
public class BalanceController {

    @Autowired
    private BalanceService balanceService;

    @PostMapping("/list")
    public Result<PageResult<BalanceRecord>> getBalanceList(@RequestBody Map<String, Object> params, @RequestHeader(value = "Authorization", required = false) String authorization) {
        Integer page = params.get("page") != null ? Integer.parseInt(params.get("page").toString()) : 1;
        Integer size = params.get("size") != null ? Integer.parseInt(params.get("size").toString()) : 10;
        Double minBalance = params.get("minBalance") != null ? Double.parseDouble(params.get("minBalance").toString()) : null;
        Double maxBalance = params.get("maxBalance") != null ? Double.parseDouble(params.get("maxBalance").toString()) : null;
        
        Long userId = null;
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            userId = TokenUtil.getUserIdFromToken(token);
        }
        
        Page<BalanceRecord> pageResult = balanceService.getBalanceList(userId, page, size, minBalance, maxBalance);
        PageResult<BalanceRecord> result = PageResult.of(pageResult.getTotal(), pageResult.getRecords());
        return Result.success(result);
    }

    @PostMapping("/reminder")
    public Result<BalanceReminderVO> getBalanceReminder(@RequestHeader("Authorization") String token) {
        Long userId = TokenUtil.getUserIdFromToken(token);
        BalanceReminderVO result = balanceService.getBalanceReminder(userId);
        return Result.success(result);
    }
}
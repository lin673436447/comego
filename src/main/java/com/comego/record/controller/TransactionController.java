package com.comego.record.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.comego.record.common.result.PageResult;
import com.comego.record.common.result.Result;
import com.comego.record.common.util.TokenUtil;
import com.comego.record.entity.TransactionRecord;
import com.comego.record.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/add")
    public Result<Map<String, Long>> addTransaction(@RequestHeader("Authorization") String token, @RequestBody TransactionRecord transactionRecord) {
        Long userId = TokenUtil.getUserIdFromToken(token);
        transactionRecord.setUserId(userId);
        transactionService.addTransaction(transactionRecord);
        Map<String, Long> result = new HashMap<>();
        result.put("transactionId", transactionRecord.getId());
        return Result.success(result);
    }

    @PostMapping("/batch-add")
    public Result<Map<String, List<Long>>> batchAddTransactions(@RequestHeader("Authorization") String token, @RequestBody List<TransactionRecord> transactionRecords) {
        Long userId = TokenUtil.getUserIdFromToken(token);
        List<Long> transactionIds = transactionService.batchAddTransactions(userId, transactionRecords);
        Map<String, List<Long>> result = new HashMap<>();
        result.put("transactionIds", transactionIds);
        return Result.success(result);
    }

    @PostMapping("/list")
    public Result<PageResult<TransactionRecord>> getTransactionList(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> params) {
        Long userId = TokenUtil.getUserIdFromToken(token);
        Integer page = params.get("page") != null ? Integer.parseInt(params.get("page").toString()) : 1;
        Integer size = params.get("size") != null ? Integer.parseInt(params.get("size").toString()) : 10;
        String startDate = params.get("startDate") != null ? params.get("startDate").toString() : null;
        String endDate = params.get("endDate") != null ? params.get("endDate").toString() : null;
        String relationType = params.get("relationType") != null ? params.get("relationType").toString() : null;
        String contactName = params.get("contactName") != null ? params.get("contactName").toString() : null;
        Long giftBookId = params.get("giftBookId") != null ? Long.parseLong(params.get("giftBookId").toString()) : null;
        Long contactId = params.get("contactId") != null ? Long.parseLong(params.get("contactId").toString()) : null;

        Page<TransactionRecord> pageResult = transactionService.getTransactionList(userId, page, size, startDate, endDate, relationType, contactName, giftBookId, contactId);
        PageResult<TransactionRecord> result = PageResult.of(pageResult.getTotal(), pageResult.getRecords());
        return Result.success(result);
    }

    @PostMapping("/update")
    public Result<Void> updateTransaction(@RequestHeader("Authorization") String token, @RequestBody TransactionRecord transactionRecord) {
        Long userId = TokenUtil.getUserIdFromToken(token);
        transactionRecord.setUserId(userId);
        transactionService.updateTransaction(transactionRecord);
        return Result.success();
    }

    @PostMapping("/delete")
    public Result<Void> deleteTransaction(@RequestHeader("Authorization") String token, @RequestBody Map<String, Long> params) {
        Long userId = TokenUtil.getUserIdFromToken(token);
        transactionService.deleteTransaction(userId, params.get("id"));
        return Result.success();
    }

    @PostMapping("/recent")
    public Result<List<TransactionRecord>> getRecentTransactions(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> params) {
        Long userId = TokenUtil.getUserIdFromToken(token);
        Integer limit = params.get("limit") != null ? Integer.parseInt(params.get("limit").toString()) : 3;
        List<TransactionRecord> recentTransactions = transactionService.getRecentTransactions(userId, limit);
        return Result.success(recentTransactions);
    }

    @PostMapping("/export")
    public Result<Map<String, String>> exportTransactions(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> params) {
        Long userId = TokenUtil.getUserIdFromToken(token);
        String startDate = params.get("startDate") != null ? params.get("startDate").toString() : null;
        String endDate = params.get("endDate") != null ? params.get("endDate").toString() : null;
        String relationType = params.get("relationType") != null ? params.get("relationType").toString() : null;
        
        String fileUrl = transactionService.exportTransactions(userId, startDate, endDate, relationType);
        Map<String, String> result = new HashMap<>();
        result.put("fileUrl", fileUrl);
        return Result.success(result);
    }

    @PostMapping("/detail")
    public Result<TransactionRecord> getTransactionDetail(@RequestHeader("Authorization") String token, @RequestBody Map<String, Long> params) {
        Long userId = TokenUtil.getUserIdFromToken(token);
        TransactionRecord transaction = transactionService.getTransactionDetail(userId, params.get("id"));
        if (transaction == null) {
            return Result.error("交易记录不存在");
        }
        return Result.success(transaction);
    }
}
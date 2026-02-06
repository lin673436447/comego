package com.comego.record.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.comego.record.common.result.PageResult;
import com.comego.record.common.result.Result;
import com.comego.record.common.util.TokenUtil;
import com.comego.record.entity.NotificationReminder;
import com.comego.record.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/list")
    public Result<PageResult<NotificationReminder>> getNotificationList(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> params) {
        Long userId = TokenUtil.getUserIdFromToken(token);
        Integer page = params.get("page") != null ? Integer.parseInt(params.get("page").toString()) : 1;
        Integer size = params.get("size") != null ? Integer.parseInt(params.get("size").toString()) : 10;
        String reminderType = params.get("reminderType") != null ? params.get("reminderType").toString() : null;
        Integer isNotified = params.get("isNotified") != null ? Integer.parseInt(params.get("isNotified").toString()) : null;
        
        Page<NotificationReminder> pageResult = notificationService.getNotificationList(userId, page, size, reminderType, isNotified);
        PageResult<NotificationReminder> result = PageResult.of(pageResult.getTotal(), pageResult.getRecords());
        return Result.success(result);
    }

    @PostMapping("/add")
    public Result<Map<String, Long>> addNotification(@RequestHeader("Authorization") String token, @RequestBody NotificationReminder notificationReminder) {
        Long userId = TokenUtil.getUserIdFromToken(token);
        notificationReminder.setUserId(userId);
        notificationService.addNotification(notificationReminder);
        Map<String, Long> result = new HashMap<>();
        result.put("notificationId", notificationReminder.getId());
        return Result.success(result);
    }

    @PostMapping("/update-status")
    public Result<Void> updateNotificationStatus(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> params) {
        Long userId = TokenUtil.getUserIdFromToken(token);
        Long id = params.get("id") != null ? Long.parseLong(params.get("id").toString()) : null;
        Integer isNotified = params.get("isNotified") != null ? Integer.parseInt(params.get("isNotified").toString()) : null;
        
        notificationService.updateNotificationStatus(userId, id, isNotified);
        return Result.success();
    }

    @PostMapping("/delete")
    public Result<Void> deleteNotification(@RequestHeader("Authorization") String token, @RequestBody Map<String, Long> params) {
        Long userId = TokenUtil.getUserIdFromToken(token);
        notificationService.deleteNotification(userId, params.get("id"));
        return Result.success();
    }
}
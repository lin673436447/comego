package com.comego.record.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.comego.record.common.exception.BusinessException;
import com.comego.record.entity.NotificationReminder;
import com.comego.record.mapper.NotificationReminderMapper;
import com.comego.record.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationReminderMapper notificationReminderMapper;

    @Override
    public Page<NotificationReminder> getNotificationList(Long userId, Integer page, Integer size, String reminderType, Integer isNotified) {
        Page<NotificationReminder> pageParam = new Page<>(page, size);
        QueryWrapper<NotificationReminder> queryWrapper = new QueryWrapper<>();
        
        queryWrapper.eq("user_id", userId);
        if (reminderType != null && !reminderType.isEmpty()) {
            queryWrapper.eq("reminder_type", reminderType);
        }
        if (isNotified != null && isNotified != 0) {
            queryWrapper.eq("is_notified", isNotified);
        }
        
        queryWrapper.eq("is_deleted", 0);
        queryWrapper.orderByDesc("reminder_date");
        
        return notificationReminderMapper.selectPage(pageParam, queryWrapper);
    }

    @Override
    public void addNotification(NotificationReminder notificationReminder) {
        notificationReminderMapper.insert(notificationReminder);
    }

    @Override
    public void updateNotificationStatus(Long userId, Long id, Integer isNotified) {
        // 验证通知是否属于该用户
        QueryWrapper<NotificationReminder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("is_deleted", 0);
        
        if (notificationReminderMapper.selectCount(queryWrapper) == 0) {
            throw new BusinessException("通知不存在或不属于该用户");
        }
        
        NotificationReminder notificationReminder = new NotificationReminder();
        notificationReminder.setId(id);
        notificationReminder.setIsNotified(isNotified);
        notificationReminderMapper.updateById(notificationReminder);
    }

    @Override
    public void deleteNotification(Long userId, Long id) {
        // 验证通知是否属于该用户
        QueryWrapper<NotificationReminder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("is_deleted", 0);
        
        if (notificationReminderMapper.selectCount(queryWrapper) == 0) {
            throw new BusinessException("通知不存在或不属于该用户");
        }
        
        NotificationReminder notificationReminder = new NotificationReminder();
        notificationReminder.setId(id);
        notificationReminder.setIsDeleted(1);
        notificationReminderMapper.updateById(notificationReminder);
    }
}
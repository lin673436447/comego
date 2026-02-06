package com.comego.record.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.comego.record.entity.NotificationReminder;

public interface NotificationService {

    Page<NotificationReminder> getNotificationList(Long userId, Integer page, Integer size, String reminderType, Integer isNotified);

    void addNotification(NotificationReminder notificationReminder);

    void updateNotificationStatus(Long userId, Long id, Integer isNotified);

    void deleteNotification(Long userId, Long id);
}
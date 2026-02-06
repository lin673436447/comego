package com.comego.record.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.comego.record.entity.ContactInfo;

import java.util.List;

public interface ContactService {

    Page<ContactInfo> getContactList(Long userId, Integer page, Integer size, String name, String relationType, Integer isFavorite);

    ContactInfo getContactDetail(Long userId, Long id);

    void addContact(ContactInfo contactInfo);

    void updateContact(ContactInfo contactInfo);

    void deleteContact(Long userId, Long id);

    List<ContactInfo> getFavorites(Long userId);
}
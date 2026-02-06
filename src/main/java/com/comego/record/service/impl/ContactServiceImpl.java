package com.comego.record.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.comego.record.common.exception.BusinessException;
import com.comego.record.entity.ContactInfo;
import com.comego.record.mapper.ContactInfoMapper;
import com.comego.record.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactInfoMapper contactInfoMapper;

    @Override
    public Page<ContactInfo> getContactList(Long userId, Integer page, Integer size, String name, String relationType, Integer isFavorite) {
        Page<ContactInfo> pageParam = new Page<>(page, size);
        QueryWrapper<ContactInfo> queryWrapper = new QueryWrapper<>();
        
        queryWrapper.eq("user_id", userId);
        if (name != null && !name.isEmpty()) {
            queryWrapper.like("name", name);
        }
        if (relationType != null && !relationType.isEmpty()) {
            queryWrapper.eq("relation_type", relationType);
        }
        if (isFavorite != null && isFavorite == 1) {
            queryWrapper.eq("is_favorite", 1);
        }
        
        queryWrapper.eq("is_deleted", 0);
        queryWrapper.orderByDesc("created_at");
        
        return contactInfoMapper.selectPage(pageParam, queryWrapper);
    }

    @Override
    public ContactInfo getContactDetail(Long userId, Long id) {
        QueryWrapper<ContactInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("is_deleted", 0);
        return contactInfoMapper.selectOne(queryWrapper);
    }

    @Override
    public void addContact(ContactInfo contactInfo) {
        contactInfoMapper.insert(contactInfo);
    }

    @Override
    public void updateContact(ContactInfo contactInfo) {
        if (contactInfo.getId() == null) {
            throw new BusinessException("联系人ID不能为空");
        }
        // 验证联系人是否属于该用户
        QueryWrapper<ContactInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", contactInfo.getId());
        queryWrapper.eq("user_id", contactInfo.getUserId());
        queryWrapper.eq("is_deleted", 0);
        
        if (contactInfoMapper.selectCount(queryWrapper) == 0) {
            throw new BusinessException("联系人不存在或不属于该用户");
        }
        contactInfoMapper.updateById(contactInfo);
    }

    @Override
    public void deleteContact(Long userId, Long id) {
        // 验证联系人是否属于该用户
        QueryWrapper<ContactInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("is_deleted", 0);
        
        if (contactInfoMapper.selectCount(queryWrapper) == 0) {
            throw new BusinessException("联系人不存在或不属于该用户");
        }
        
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setId(id);
        contactInfo.setIsDeleted(1);
        contactInfoMapper.updateById(contactInfo);
    }

    @Override
    public List<ContactInfo> getFavorites(Long userId) {
        QueryWrapper<ContactInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("is_favorite", 1);
        queryWrapper.eq("is_deleted", 0);
        queryWrapper.orderByDesc("created_at");
        return contactInfoMapper.selectList(queryWrapper);
    }
}
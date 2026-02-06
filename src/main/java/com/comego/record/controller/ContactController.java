package com.comego.record.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.comego.record.common.result.PageResult;
import com.comego.record.common.result.Result;
import com.comego.record.common.util.TokenUtil;
import com.comego.record.entity.ContactInfo;
import com.comego.record.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/contact")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping("/list")
    public Result<PageResult<ContactInfo>> getContactList(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> params) {
        Long userId = TokenUtil.getUserIdFromToken(token);
        Integer page = params.get("page") != null ? Integer.parseInt(params.get("page").toString()) : 1;
        Integer size = params.get("size") != null ? Integer.parseInt(params.get("size").toString()) : 10;
        String name = params.get("name") != null ? params.get("name").toString() : null;
        String relationType = params.get("relationType") != null ? params.get("relationType").toString() : null;
        Integer isFavorite = params.get("isFavorite") != null ? Integer.parseInt(params.get("isFavorite").toString()) : null;
        
        Page<ContactInfo> pageResult = contactService.getContactList(userId, page, size, name, relationType, isFavorite);
        PageResult<ContactInfo> result = PageResult.of(pageResult.getTotal(), pageResult.getRecords());
        return Result.success(result);
    }

    @PostMapping("/add")
    public Result<Map<String, Long>> addContact(@RequestHeader("Authorization") String token, @RequestBody ContactInfo contactInfo) {
        Long userId = TokenUtil.getUserIdFromToken(token);
        contactInfo.setUserId(userId);
        contactService.addContact(contactInfo);
        Map<String, Long> result = new HashMap<>();
        result.put("contactId", contactInfo.getId());
        return Result.success(result);
    }

    @PostMapping("/update")
    public Result<Void> updateContact(@RequestHeader("Authorization") String token, @RequestBody ContactInfo contactInfo) {
        Long userId = TokenUtil.getUserIdFromToken(token);
        contactInfo.setUserId(userId);
        contactService.updateContact(contactInfo);
        return Result.success();
    }

    @PostMapping("/delete")
    public Result<Void> deleteContact(@RequestHeader("Authorization") String token, @RequestBody Map<String, Long> params) {
        Long userId = TokenUtil.getUserIdFromToken(token);
        contactService.deleteContact(userId, params.get("id"));
        return Result.success();
    }

    @PostMapping("/favorites")
    public Result<List<ContactInfo>> getFavorites(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> params) {
        Long userId = TokenUtil.getUserIdFromToken(token);
        List<ContactInfo> favorites = contactService.getFavorites(userId);
        return Result.success(favorites);
    }

    @PostMapping("/detail")
    public Result<ContactInfo> getContactDetail(@RequestHeader("Authorization") String token, @RequestBody Map<String, Long> params) {
        Long userId = TokenUtil.getUserIdFromToken(token);
        ContactInfo contact = contactService.getContactDetail(userId, params.get("id"));
        if (contact == null) {
            return Result.error("联系人不存在");
        }
        return Result.success(contact);
    }
}
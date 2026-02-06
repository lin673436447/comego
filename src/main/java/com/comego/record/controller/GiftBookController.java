package com.comego.record.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.comego.record.common.result.PageResult;
import com.comego.record.common.result.Result;
import com.comego.record.common.util.TokenUtil;
import com.comego.record.entity.GiftBook;
import com.comego.record.service.GiftBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/giftbook")
public class GiftBookController {

    @Autowired
    private GiftBookService giftBookService;

    @PostMapping("/list")
    public Result<PageResult<GiftBook>> getGiftBookList(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> params) {
        Long userId = TokenUtil.getUserIdFromToken(token);
        Integer page = params.get("page") != null ? Integer.parseInt(params.get("page").toString()) : 1;
        Integer size = params.get("size") != null ? Integer.parseInt(params.get("size").toString()) : 10;
        String bookType = params.get("bookType") != null ? params.get("bookType").toString() : null;
        String bookName = params.get("bookName") != null ? params.get("bookName").toString() : null;
        
        Page<GiftBook> pageResult = giftBookService.getGiftBookList(userId, page, size, bookType, bookName);
        PageResult<GiftBook> result = PageResult.of(pageResult.getTotal(), pageResult.getRecords());
        return Result.success(result);
    }

    @PostMapping("/add")
    public Result<Map<String, Long>> addGiftBook(@RequestHeader("Authorization") String token, @RequestBody GiftBook giftBook) {
        Long userId = TokenUtil.getUserIdFromToken(token);
        giftBook.setUserId(userId);
        giftBookService.addGiftBook(giftBook);
        Map<String, Long> result = new HashMap<>();
        result.put("giftBookId", giftBook.getId());
        return Result.success(result);
    }

    @PostMapping("/update")
    public Result<Void> updateGiftBook(@RequestHeader("Authorization") String token, @RequestBody GiftBook giftBook) {
        Long userId = TokenUtil.getUserIdFromToken(token);
        giftBook.setUserId(userId);
        giftBookService.updateGiftBook(giftBook);
        return Result.success();
    }

    @PostMapping("/delete")
    public Result<Void> deleteGiftBook(@RequestHeader("Authorization") String token, @RequestBody Map<String, Long> params) {
        Long userId = TokenUtil.getUserIdFromToken(token);
        giftBookService.deleteGiftBook(userId, params.get("id"));
        return Result.success();
    }

    @PostMapping("/active")
    public Result<List<GiftBook>> getActiveGiftBooks(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> params) {
        Long userId = TokenUtil.getUserIdFromToken(token);
        List<GiftBook> activeGiftBooks = giftBookService.getActiveGiftBooks(userId);
        return Result.success(activeGiftBooks);
    }

    @PostMapping("/detail")
    public Result<GiftBook> getGiftBookDetail(@RequestHeader("Authorization") String token, @RequestBody Map<String, Long> params) {
        Long userId = TokenUtil.getUserIdFromToken(token);
        GiftBook giftBook = giftBookService.getGiftBookDetail(userId, params.get("id"));
        if (giftBook == null) {
            return Result.error("礼簿不存在");
        }
        return Result.success(giftBook);
    }

    @PostMapping("/recent-with-records")
    public Result<List<GiftBook>> getRecentGiftBooksWithRecords(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> params) {
        Long userId = TokenUtil.getUserIdFromToken(token);
        Integer limit = params.get("limit") != null ? Integer.parseInt(params.get("limit").toString()) : 5;
        List<GiftBook> giftBooks = giftBookService.getRecentGiftBooksWithRecords(userId, limit);
        return Result.success(giftBooks);
    }
}

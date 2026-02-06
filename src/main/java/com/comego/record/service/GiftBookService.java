package com.comego.record.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.comego.record.entity.GiftBook;

import java.util.List;

public interface GiftBookService {

    Page<GiftBook> getGiftBookList(Long userId, Integer page, Integer size, String bookType, String bookName);

    GiftBook getGiftBookDetail(Long userId, Long id);

    void addGiftBook(GiftBook giftBook);

    void updateGiftBook(GiftBook giftBook);

    void deleteGiftBook(Long userId, Long id);

    List<GiftBook> getActiveGiftBooks(Long userId);

    void updateGiftBookStats(Long giftBookId);

    List<GiftBook> getRecentGiftBooksWithRecords(Long userId, Integer limit);
}

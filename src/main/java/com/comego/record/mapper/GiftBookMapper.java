package com.comego.record.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.comego.record.entity.GiftBook;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GiftBookMapper extends BaseMapper<GiftBook> {

    @Select("SELECT gb.* FROM gift_book gb " +
            "INNER JOIN (" +
            "    SELECT gift_book_id, MAX(created_at) as last_record_time " +
            "    FROM transaction_record " +
            "    WHERE user_id = #{userId} AND is_deleted = 0 " +
            "    GROUP BY gift_book_id " +
            ") tr ON gb.id = tr.gift_book_id " +
            "WHERE gb.user_id = #{userId} " +
            "AND gb.is_deleted = 0 " +
            "ORDER BY tr.last_record_time DESC " +
            "LIMIT #{limit}")
    List<GiftBook> selectRecentGiftBooksWithRecords(@Param("userId") Long userId, @Param("limit") Integer limit);
}

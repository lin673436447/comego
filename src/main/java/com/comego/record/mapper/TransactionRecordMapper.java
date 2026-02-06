package com.comego.record.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.comego.record.entity.TransactionRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TransactionRecordMapper extends BaseMapper<TransactionRecord> {
}
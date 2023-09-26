package com.wansensoft.mappers.serialNumber;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wansensoft.entities.Sequence;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SequenceMapperEx extends BaseMapper<Sequence> {

    void updateBuildOnlyNumber();

    /**
     * 获得一个全局唯一的数作为订单号的追加
     * */
    Long getBuildOnlyNumber(@Param("seq_name") String seq_name);
}

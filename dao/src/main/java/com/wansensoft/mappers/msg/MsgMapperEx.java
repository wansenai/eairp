package com.wansensoft.mappers.msg;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wansensoft.entities.msg.Msg;
import com.wansensoft.entities.msg.MsgEx;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MsgMapperEx extends BaseMapper<Msg> {

    List<MsgEx> selectByConditionMsg(
            @Param("userId") Long userId,
            @Param("name") String name,
            @Param("offset") Integer offset,
            @Param("rows") Integer rows);

    Long countsByMsg(
            @Param("userId") Long userId,
            @Param("name") String name);

    int batchDeleteMsgByIds(@Param("ids") String ids[]);

    Long getMsgCountByStatus(
            @Param("status") String status,
            @Param("userId") Long userId);
}
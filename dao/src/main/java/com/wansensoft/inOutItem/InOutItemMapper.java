package com.wansensoft.inOutItem;

import com.wansensoft.entities.inOutItem.InOutItem;
import com.wansensoft.entities.inOutItem.InOutItemExample;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface InOutItemMapper {
    long countByExample(InOutItemExample example);

    int deleteByExample(InOutItemExample example);

    int deleteByPrimaryKey(Long id);

    int insert(InOutItem record);

    int insertSelective(InOutItem record);

    List<InOutItem> selectByExample(InOutItemExample example);

    InOutItem selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") InOutItem record, @Param("example") InOutItemExample example);

    int updateByExample(@Param("record") InOutItem record, @Param("example") InOutItemExample example);

    int updateByPrimaryKeySelective(InOutItem record);

    int updateByPrimaryKey(InOutItem record);
}
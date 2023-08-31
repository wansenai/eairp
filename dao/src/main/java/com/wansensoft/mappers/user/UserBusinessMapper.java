package com.wansensoft.mappers.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wansensoft.entities.user.UserBusiness;
import com.wansensoft.entities.user.UserBusinessExample;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserBusinessMapper extends BaseMapper<UserBusiness> {
    long countByExample(UserBusinessExample example);

    int deleteByExample(UserBusinessExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UserBusiness record);

    int insertSelective(UserBusiness record);

    List<UserBusiness> selectByExample(UserBusinessExample example);

    UserBusiness selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UserBusiness record, @Param("example") UserBusinessExample example);

    int updateByExample(@Param("record") UserBusiness record, @Param("example") UserBusinessExample example);

    int updateByPrimaryKeySelective(UserBusiness record);

    int updateByPrimaryKey(UserBusiness record);
}
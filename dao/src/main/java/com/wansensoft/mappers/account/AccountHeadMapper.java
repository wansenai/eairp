package com.wansensoft.mappers.account;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wansensoft.entities.account.AccountHead;
import com.wansensoft.entities.account.AccountHeadExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AccountHeadMapper extends BaseMapper<AccountHead> {
    long countByExample(AccountHeadExample example);

    int deleteByExample(AccountHeadExample example);

    int deleteByPrimaryKey(Long id);

    int insert(AccountHead record);

    int insertSelective(AccountHead record);

    List<AccountHead> selectByExample(AccountHeadExample example);

    AccountHead selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") AccountHead record, @Param("example") AccountHeadExample example);

    int updateByExample(@Param("record") AccountHead record, @Param("example") AccountHeadExample example);

    int updateByPrimaryKeySelective(AccountHead record);

    int updateByPrimaryKey(AccountHead record);
}
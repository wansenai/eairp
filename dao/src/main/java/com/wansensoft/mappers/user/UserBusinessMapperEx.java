package com.wansensoft.mappers.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wansensoft.entities.user.UserBusiness;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Date;

@Mapper
public interface UserBusinessMapperEx extends BaseMapper<UserBusiness> {

    int batchDeleteUserBusinessByIds(@Param("updateTime") Date updateTime, @Param("updater") Long updater, @Param("ids") String ids[]);

}

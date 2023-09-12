package com.wansensoft.service.financial;

import com.wansensoft.entities.financial.FinancialAccount;
import com.wansensoft.mappers.financial.FinancialAccountMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 账户信息 服务实现类
 * </p>
 *
 * @author James Zow
 * @since 2023-09-05
 */
@Service
public class FinancialAccountServiceImpl extends ServiceImpl<FinancialAccountMapper, FinancialAccount> implements IFinancialAccountService {

}

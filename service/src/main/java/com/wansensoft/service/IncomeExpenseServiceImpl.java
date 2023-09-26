package com.wansensoft.service;

import com.wansensoft.entities.IncomeExpense;
import com.wansensoft.mappers.IncomeExpenseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 收支项目 服务实现类
 * </p>
 *
 * @author James Zow
 * @since 2023-09-05
 */
@Service
public class IncomeExpenseServiceImpl extends ServiceImpl<IncomeExpenseMapper, IncomeExpense> implements IIncomeExpenseService {

}

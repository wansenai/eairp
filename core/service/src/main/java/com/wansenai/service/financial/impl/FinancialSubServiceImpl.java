package com.wansenai.service.financial.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansenai.entities.financial.FinancialSub;
import com.wansenai.mappers.financial.FinancialSubMapper;
import com.wansenai.service.financial.FinancialSubService;
import org.springframework.stereotype.Service;

@Service
public class FinancialSubServiceImpl extends ServiceImpl<FinancialSubMapper, FinancialSub> implements FinancialSubService {
}

package com.wansensoft.service.sequence;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansensoft.entities.Sequence;
import com.wansensoft.entities.serialNumber.SerialNumber;
import com.wansensoft.entities.serialNumber.SerialNumberEx;
import com.wansensoft.mappers.serialNumber.SequenceMapperEx;
import com.wansensoft.utils.constants.BusinessConstants;
import com.wansensoft.plugins.exception.JshException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Description
 */
@Service
public class SequenceServiceImpl extends ServiceImpl<SequenceMapperEx, Sequence> implements SequenceService{
    private Logger logger = LoggerFactory.getLogger(SequenceServiceImpl.class);

    private final SequenceMapperEx sequenceMapperEx;

    public SequenceServiceImpl(SequenceMapperEx sequenceMapperEx) {
        this.sequenceMapperEx = sequenceMapperEx;
    }

    public SerialNumber getSequence(long id) {
        return null;
    }

    public List<SerialNumberEx> select(String name, Integer offset, Integer rows) {
        return null;
    }

    public Long countSequence(String name) {
        return null;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int insertSequence(JSONObject obj, HttpServletRequest request) {
        return 0;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int updateSequence(JSONObject obj, HttpServletRequest request) {
        return 0;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int deleteSequence(Long id, HttpServletRequest request) {
        return 0;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteSequence(String ids, HttpServletRequest request) {
        return 0;
    }

    public int checkIsNameExist(Long id, String serialNumber) {
        return 0;
    }

    /**
     * 创建一个唯一的序列号
     * */
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public String buildOnlyNumber() {
        Long buildOnlyNumber=null;
        synchronized (this){
            try{
                sequenceMapperEx.updateBuildOnlyNumber(); //编号+1
                buildOnlyNumber= sequenceMapperEx.getBuildOnlyNumber(BusinessConstants.DEPOT_NUMBER_SEQ);
            }catch(Exception e){
                JshException.writeFail(logger, e);
            }
        }
        if(buildOnlyNumber<BusinessConstants.SEQ_TO_STRING_MIN_LENGTH){
            StringBuffer sb=new StringBuffer(buildOnlyNumber.toString());
            int len=BusinessConstants.SEQ_TO_STRING_MIN_LENGTH.toString().length()-sb.length();
            for(int i=0;i<len;i++){
                sb.insert(0,BusinessConstants.SEQ_TO_STRING_LESS_INSERT);
            }
            return sb.toString();
        }else{
            return buildOnlyNumber.toString();
        }
    }
}

package com.wansensoft.service.materialCategory;

import com.alibaba.fastjson.JSONObject;
import com.wansensoft.entities.material.Material;
import com.wansensoft.entities.material.MaterialCategory;
import com.wansensoft.entities.material.MaterialCategoryExample;
import com.wansensoft.entities.user.User;
import com.wansensoft.mappers.material.MaterialCategoryMapper;
import com.wansensoft.mappers.material.MaterialCategoryMapperEx;
import com.wansensoft.mappers.material.MaterialMapperEx;
import com.wansensoft.service.log.LogServiceImpl;
import com.wansensoft.service.user.UserServiceImpl;
import com.wansensoft.utils.constants.BusinessConstants;
import com.wansensoft.utils.constants.ExceptionConstants;
import com.wansensoft.plugins.exception.BusinessRunTimeException;
import com.wansensoft.plugins.exception.JshException;
import com.wansensoft.utils.StringUtil;
import com.wansensoft.vo.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MaterialCategoryService {
    private Logger logger = LoggerFactory.getLogger(MaterialCategoryService.class);

    private final MaterialCategoryMapper materialCategoryMapper;
    private final MaterialCategoryMapperEx materialCategoryMapperEx;
    private final UserServiceImpl userServiceImpl;
    private final LogServiceImpl logServiceImpl;
    private final MaterialMapperEx materialMapperEx;

    public MaterialCategoryService(MaterialCategoryMapper materialCategoryMapper, MaterialCategoryMapperEx materialCategoryMapperEx, UserServiceImpl userServiceImpl, LogServiceImpl logServiceImpl, MaterialMapperEx materialMapperEx) {
        this.materialCategoryMapper = materialCategoryMapper;
        this.materialCategoryMapperEx = materialCategoryMapperEx;
        this.userServiceImpl = userServiceImpl;
        this.logServiceImpl = logServiceImpl;
        this.materialMapperEx = materialMapperEx;
    }

    public MaterialCategory getMaterialCategory(long id)throws Exception {
        MaterialCategory result=null;
        try{
            result=materialCategoryMapper.selectByPrimaryKey(id);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }

    public List<MaterialCategory> getMaterialCategoryListByIds(String ids)throws Exception {
        List<Long> idList = StringUtil.strToLongList(ids);
        List<MaterialCategory> list = new ArrayList<>();
        try{
            MaterialCategoryExample example = new MaterialCategoryExample();
            example.createCriteria().andIdIn(idList);
            list = materialCategoryMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public List<MaterialCategory> getMaterialCategory()throws Exception {
        MaterialCategoryExample example = new MaterialCategoryExample();
        List<MaterialCategory> list=null;
        try{
            list=materialCategoryMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public List<MaterialCategory> getAllList(Long parentId)throws Exception {
        List<MaterialCategory> list=null;
        try{
            list = getMCList(parentId);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public List<MaterialCategory> getMCList(Long parentId)throws Exception {
        List<MaterialCategory> res= new ArrayList<MaterialCategory>();
        List<MaterialCategory> list=null;
        MaterialCategoryExample example = new MaterialCategoryExample();
        example.createCriteria().andParentIdEqualTo(parentId).andIdNotEqualTo(1L);
        example.setOrderByClause("id");
        list=materialCategoryMapper.selectByExample(example);
        if(list!=null && list.size()>0) {
            res.addAll(list);
            for(MaterialCategory mc : list) {
                List<MaterialCategory> mcList = getMCList(mc.getId());
                if(mcList!=null && mcList.size()>0) {
                    res.addAll(mcList);
                }
            }
        }
        return res;
    }

    public List<MaterialCategory> select(String name, Integer parentId, int offset, int rows) throws Exception{
        List<MaterialCategory> list=null;
        try{
            list=materialCategoryMapperEx.selectByConditionMaterialCategory(name, parentId, offset, rows);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public Long countMaterialCategory(String name, Integer parentId) throws Exception{
        Long result=null;
        try{
            result=materialCategoryMapperEx.countsByMaterialCategory(name, parentId);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int insertMaterialCategory(JSONObject obj, HttpServletRequest request)throws Exception {
        MaterialCategory materialCategory = JSONObject.parseObject(obj.toJSONString(), MaterialCategory.class);
        materialCategory.setCreateTime(new Date());
        materialCategory.setUpdateTime(new Date());
        int result=0;
        try{
            result=materialCategoryMapper.insertSelective(materialCategory);
            logServiceImpl.insertLog("商品类型",
                    new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_ADD).append(materialCategory.getName()).toString(), request);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int updateMaterialCategory(JSONObject obj, HttpServletRequest request) throws Exception{
        MaterialCategory materialCategory = JSONObject.parseObject(obj.toJSONString(), MaterialCategory.class);
        materialCategory.setUpdateTime(new Date());
        int result=0;
        try{
            result=materialCategoryMapper.updateByPrimaryKeySelective(materialCategory);
            logServiceImpl.insertLog("商品类型",
                    new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_EDIT).append(materialCategory.getName()).toString(), request);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int deleteMaterialCategory(Long id, HttpServletRequest request)throws Exception {
        return batchDeleteMaterialCategoryByIds(id.toString());
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteMaterialCategory(String ids, HttpServletRequest request)throws Exception {
        return batchDeleteMaterialCategoryByIds(ids);
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteMaterialCategoryByIds(String ids) throws Exception {
        int result=0;
        String [] idArray=ids.split(",");
        //校验产品表	jsh_material
        List<Material> materialList=null;
        try{
            materialList= materialMapperEx.getMaterialListByCategoryIds(idArray);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        if(materialList!=null&&materialList.size()>0){
            logger.error("异常码[{}],异常提示[{}],参数,CategoryIds[{}]",
                    ExceptionConstants.DELETE_FORCE_CONFIRM_CODE,ExceptionConstants.DELETE_FORCE_CONFIRM_MSG,ids);
            throw new BusinessRunTimeException(ExceptionConstants.DELETE_FORCE_CONFIRM_CODE,
                    ExceptionConstants.DELETE_FORCE_CONFIRM_MSG);
        }
        StringBuffer sb = new StringBuffer();
        sb.append(BusinessConstants.LOG_OPERATION_TYPE_DELETE);
        List<MaterialCategory> list = getMaterialCategoryListByIds(ids);
        for(MaterialCategory materialCategory: list){
            sb.append("[").append(materialCategory.getName()).append("]");
        }
        logServiceImpl.insertLog("商品类型", sb.toString(),
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        //更新时间
        Date updateDate =new Date();
        //更新人
        User userInfo= userServiceImpl.getCurrentUser();
        Long updater=userInfo==null?null:userInfo.getId();
        String strArray[]=ids.split(",");
        if(strArray.length<1){
            return 0;
        }
        List<MaterialCategory> mcList = materialCategoryMapperEx.getMaterialCategoryListByCategoryIds(idArray);
        if(mcList!=null && mcList.size()>0) {
            logger.error("异常码[{}],异常提示[{}]",
                    ExceptionConstants.MATERIAL_CATEGORY_CHILD_NOT_SUPPORT_DELETE_CODE,ExceptionConstants.MATERIAL_CATEGORY_CHILD_NOT_SUPPORT_DELETE_MSG);
            throw new BusinessRunTimeException(ExceptionConstants.MATERIAL_CATEGORY_CHILD_NOT_SUPPORT_DELETE_CODE,
                    ExceptionConstants.MATERIAL_CATEGORY_CHILD_NOT_SUPPORT_DELETE_MSG);
        } else {
            result=materialCategoryMapperEx.batchDeleteMaterialCategoryByIds(updateDate,updater,strArray);
        }
        return result;
    }

    public int checkIsNameExist(Long id, String name)throws Exception {
        MaterialCategoryExample example = new MaterialCategoryExample();
        example.createCriteria().andIdNotEqualTo(id).andNameEqualTo(name).andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        List<MaterialCategory> list=null;
        try{
            list= materialCategoryMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list==null?0:list.size();
    }

    public List<MaterialCategory> findById(Long id)throws Exception {
        List<MaterialCategory> list=null;
        if(id!=null) {
            MaterialCategoryExample example = new MaterialCategoryExample();
            example.createCriteria().andIdEqualTo(id);
            try{
                list=materialCategoryMapper.selectByExample(example);
            }catch(Exception e){
                JshException.readFail(logger, e);
            }
        }
        return list;
    }
    /**
     *获取商品类别树数据
     * @return java.util.List<com.jsh.erp.datasource.vo.TreeNode>
     */
    public List<TreeNode> getMaterialCategoryTree(Long id) throws Exception{
        List<TreeNode> list=null;
        try{
            list=materialCategoryMapperEx.getNodeTree(id);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
       return list;
    }
    /**
     * create by: cjl
     * description:
     *  新增商品类别信息
     * create time: 2019/2/19 16:30
     * @Param: mc
     * @return void
     */
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int addMaterialCategory(MaterialCategory mc) throws Exception {
        logServiceImpl.insertLog("商品类型",
                new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_ADD).append(mc.getName()).toString(),
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        if(mc==null){
            return 0;
        }
        if(mc.getParentId()==null){
            //没有给定父级目录的id，默认设置父级目录为根目录的父目录
            mc.setParentId(BusinessConstants.MATERIAL_CATEGORY_ROOT_PARENT_ID);
        }
        //检查商品类型编号是否已存在
        checkMaterialCategorySerialNo(mc);
        //数据状态新增时默认设置为启用
        Date date=new Date();
        User userInfo= userServiceImpl.getCurrentUser();
        //创建时间
        mc.setCreateTime(date);
        //更新时间
        mc.setUpdateTime(date);
        int result=0;
        try{
            result=materialCategoryMapperEx.addMaterialCategory(mc);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int editMaterialCategory(MaterialCategory mc) throws Exception{
        logServiceImpl.insertLog("商品类型",
                new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_EDIT).append(mc.getName()).toString(),
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        if(mc.getParentId()==null){
            //没有给定父级目录的id，默认设置父级目录为根目录的父目录
            mc.setParentId(BusinessConstants.MATERIAL_CATEGORY_ROOT_PARENT_ID);
        }
        //检查商品类型编号是否已存在
        checkMaterialCategorySerialNo(mc);
        //更新时间
        mc.setUpdateTime(new Date());
        //更新人
        User userInfo= userServiceImpl.getCurrentUser();
        int result=0;
        try{
            result= materialCategoryMapperEx.editMaterialCategory(mc);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }
    /**
     * 根据商品类别编号判断商品类别是否已存在
     * */
    public void  checkMaterialCategorySerialNo(MaterialCategory mc)throws Exception {
        if(mc==null){
            return;
        }
        if(StringUtil.isEmpty(mc.getSerialNo())){
            return;
        }
        //根据商品类别编号查询商品类别
        List<MaterialCategory> mList=null;
        try{
            mList= materialCategoryMapperEx.getMaterialCategoryBySerialNo(mc.getSerialNo(), mc.getId());
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        if(mList==null||mList.size()<1){
            //未查询到对应数据，编号可用
            return;
        }
        if(mList.size()>1){
            //查询到的数据条数大于1，编号已存在
            throw new BusinessRunTimeException(ExceptionConstants.MATERIAL_CATEGORY_SERIAL_ALREADY_EXISTS_CODE,
                    ExceptionConstants.MATERIAL_CATEGORY_SERIAL_ALREADY_EXISTS_MSG);
        }
        if(mc.getId()==null){
            //新增时，编号已存在
            throw new BusinessRunTimeException(ExceptionConstants.MATERIAL_CATEGORY_SERIAL_ALREADY_EXISTS_CODE,
                    ExceptionConstants.MATERIAL_CATEGORY_SERIAL_ALREADY_EXISTS_MSG);
        }
        /**
         * 包装类型用equals来比较
         * */
        if(mc.getId().equals(mList.get(0).getId())){
            //修改时，相同编号，id不同
            throw new BusinessRunTimeException(ExceptionConstants.MATERIAL_CATEGORY_SERIAL_ALREADY_EXISTS_CODE,
                    ExceptionConstants.MATERIAL_CATEGORY_SERIAL_ALREADY_EXISTS_MSG);
        }
    }

    /**
     * 根据名称获取类型
     * @param name
     */
    public Long getCategoryIdByName(String name){
        Long categoryId = null;
        MaterialCategoryExample example = new MaterialCategoryExample();
        example.createCriteria().andNameEqualTo(name).andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        List<MaterialCategory> list = materialCategoryMapper.selectByExample(example);
        if(list!=null && list.size()>0) {
            categoryId = list.get(0).getId();
        }
        return categoryId;
    }
}

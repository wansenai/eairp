package com.wansensoft.api.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.wansensoft.utils.Tools;
import jakarta.servlet.http.HttpServletRequest;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.wansensoft.mappers")
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(HttpServletRequest request) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor();
        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new TenantLineHandler() {
            @Override
            public Expression getTenantId() {
                String token = request.getHeader("X-Access-Token");
                Long tenantId = Tools.getTenantIdByToken(token);
                if (tenantId!=0L) {
                    return new LongValue(tenantId);
                } else {
                    //超管
                    return null;
                }
            }

            // 这是 default 方法,默认返回 false 表示所有表都需要拼多租户条件
            @Override
            public boolean ignoreTable(String tableName) {
                //获取开启状态
                boolean res = true;
                String token = request.getHeader("X-Access-Token");
                Long tenantId = Tools.getTenantIdByToken(token);
                if (tenantId!=0L) {
                    // 这里可以判断是否过滤表
                    if ("jsh_material_property".equals(tableName) || "jsh_sequence".equals(tableName)
                            || "jsh_user_business".equals(tableName) || "jsh_function".equals(tableName)
                            || "jsh_platform_config".equals(tableName) || "jsh_tenant".equals(tableName)) {
                        res = true;
                    } else {
                        res = false;
                    }
                }
                return res;
            }
        }));


        // 如果用了分页插件注意先 add TenantLineInnerInterceptor 再 add PaginationInnerInterceptor
        // 用了分页插件必须设置 MybatisConfiguration#useDeprecatedExecutor = false
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

//    @Bean
//    public PaginationInnerInterceptor paginationInterceptor(HttpServletRequest request) {
//        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
//
//        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor();
//        List<ISqlParser> sqlParserList = new ArrayList<>();
//        TenantSqlParser tenantSqlParser = new TenantSqlParser();
//        tenantSqlParser.setTenantHandler(new TenantHandler() {
//            @Override
//            public Expression getTenantId() {
//                String token = request.getHeader("X-Access-Token");
//                Long tenantId = Tools.getTenantIdByToken(token);
//                if (tenantId!=0L) {
//                    return new LongValue(tenantId);
//                } else {
//                    //超管
//                    return null;
//                }
//            }
//
//            @Override
//            public String getTenantIdColumn() {
//                return "tenant_id";
//            }
//
//            @Override
//            public boolean doTableFilter(String tableName) {
//                //获取开启状态
//                Boolean res = true;
//                String token = request.getHeader("X-Access-Token");
//                Long tenantId = Tools.getTenantIdByToken(token);
//                if (tenantId!=0L) {
//                    // 这里可以判断是否过滤表
//                    if ("jsh_material_property".equals(tableName) || "jsh_sequence".equals(tableName)
//                            || "jsh_user_business".equals(tableName) || "jsh_function".equals(tableName)
//                            || "jsh_platform_config".equals(tableName) || "jsh_tenant".equals(tableName)) {
//                        res = true;
//                    } else {
//                        res = false;
//                    }
//                }
//                return res;
//            }
//        });
//
//        sqlParserList.add(tenantSqlParser);
//        paginationInterceptor.setSqlParserList(sqlParserList);
//        paginationInterceptor.setSqlParserFilter(new ISqlParserFilter() {
//            @Override
//            public boolean doFilter(MetaObject metaObject) {
//                MappedStatement ms = SqlParserHelper.getMappedStatement(metaObject);
//                // 过滤自定义查询此时无租户信息约束出现
//                if ("com.jsh.erp.datasource.mappers.UserMapperEx.getUserByWeixinOpenId".equals(ms.getId())) {
//                    return true;
//                } else if ("com.jsh.erp.datasource.mappers.UserMapperEx.updateUserWithWeixinOpenId".equals(ms.getId())) {
//                    return true;
//                } else if ("com.jsh.erp.datasource.mappers.UserMapperEx.getUserListByUserNameOrLoginName".equals(ms.getId())) {
//                    return true;
//                } else if ("com.jsh.erp.datasource.mappers.UserMapperEx.disableUserByLimit".equals(ms.getId())) {
//                    return true;
//                } else if ("com.jsh.erp.datasource.mappers.RoleMapperEx.getRoleWithoutTenant".equals(ms.getId())) {
//                    return true;
//                } else if ("com.jsh.erp.datasource.mappers.LogMapperEx.insertLogWithUserId".equals(ms.getId())) {
//                    return true;
//                }
//                return false;
//            }
//        });
//        return paginationInterceptor;
//    }

    /**
     * 相当于顶部的：
     * {@code @MapperScan("com.wansensoft.erp.datasource.mappers*")}
     * 这里可以扩展，比如使用配置文件来配置扫描Mapper的路径
     */
//    @Bean
//    public MapperScannerConfigurer mapperScannerConfigurer() {
//        MapperScannerConfigurer scannerConfigurer = new MapperScannerConfigurer();
//        scannerConfigurer.setBasePackage("com.wansensoft.mappers*");
//        return scannerConfigurer;
//    }

    /**
     * 性能分析拦截器，不建议生产使用
     */
//    @Bean
//    public PerformanceInterceptor performanceInterceptor(){
//        return new PerformanceInterceptor();
//    }

}

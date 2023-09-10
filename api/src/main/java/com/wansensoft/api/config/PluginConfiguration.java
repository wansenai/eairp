//package com.wansensoft.api.config;
//
//import com.gitee.starblues.core.RuntimeMode;
//import com.gitee.starblues.integration.DefaultIntegrationConfiguration;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.stereotype.Component;
///**
// *
// */
//@Component
//@ConfigurationProperties(prefix = "plugin")
//public class PluginConfiguration extends DefaultIntegrationConfiguration {
//    /**
//     * 运行模式
//     *  开发环境: development、dev
//     *  生产/部署 环境: deployment、prod
//     */
//    @Value("${runMode:dev}")
//    private String runMode;
//
//    @Value("${pluginPath:plugins}")
//    private String pluginPath;
//
//    @Value("${pluginConfigFilePath:pluginConfigs}")
//    private String pluginConfigFilePath;
//
//    @Override
//    public RuntimeMode environment() {
//        return RuntimeMode.byName(runMode);
//    }
//
//    @Override
//    public String mainPackage() {
//        return null;
//    }
//
//    public String getRunMode() {
//        return runMode;
//    }
//
//    public void setRunMode(String runMode) {
//        this.runMode = runMode;
//    }
//
//
//    public String getPluginPath() {
//        return pluginPath;
//    }
//
//    public void setPluginPath(String pluginPath) {
//        this.pluginPath = pluginPath;
//    }
//
//    public String getPluginConfigFilePath() {
//        return pluginConfigFilePath;
//    }
//
//    public void setPluginConfigFilePath(String pluginConfigFilePath) {
//        this.pluginConfigFilePath = pluginConfigFilePath;
//    }
//
//    @Override
//    public String toString() {
//        return "PluginArgConfiguration{" +
//                "runMode='" + runMode + '\'' +
//                ", pluginPath='" + pluginPath + '\'' +
//                ", pluginConfigFilePath='" + pluginConfigFilePath + '\'' +
//                '}';
//    }
//}

package com.message.system;

import org.apache.commons.io.IOUtils;
import org.apache.shiro.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;

import java.util.Map;

/**
 * 扩展Shiro Filter Chain定义
 * @Author Alcott Hawk
 * @Date 12/1/2016
 */
public class ExtensibleFilterChainDefinitions implements FactoryBean<String>{

    private static final String NEW_LINE = "\n";

    private static final Logger LOGGER = LoggerFactory.getLogger(ExtensibleFilterChainDefinitions.class);

    private String filterChainDefinitions;

    private Resource[] locations;

    private Map<String, String> filterChainDefinitionMap;

    public String getObject() throws Exception {
        StringBuilder builder = new StringBuilder();
        //首先读取Spring XML内字符串方式配置
        if (filterChainDefinitions != null) {
            builder.append(StringUtils.clean(filterChainDefinitions)).append(NEW_LINE);
        }
        //再读取Map方式配置
        if( filterChainDefinitionMap != null ) {
            for (String key : filterChainDefinitionMap.keySet()) {
                builder.append(key)
                        .append("=")
                        .append(filterChainDefinitionMap.get(key))
                        .append(NEW_LINE);
            }
        }
        //最后读取properties配置文件方式
        if(locations != null) {
            for (Resource resource : locations) {
                if (resource != null && resource.exists() && resource.isReadable()) {
                    String string = IOUtils.toString(resource.getInputStream());
                    builder.append(string).append(NEW_LINE);
                }
            }
        }
        String config = builder.toString();
        LOGGER.debug("自定义Shiro 扩展配置加载完成：\n{}", config);
        return config;
    }

    public Class<?> getObjectType() {
        return getClass();
    }

    public boolean isSingleton() {
        return false;
    }

    public String getFilterChainDefinitions() {
        return filterChainDefinitions;
    }

    public void setFilterChainDefinitions(String filterChainDefinitions) {
        this.filterChainDefinitions = filterChainDefinitions;
    }

    public Map<String, String> getFilterChainDefinitionMap() {
        return filterChainDefinitionMap;
    }

    public void setFilterChainDefinitionMap(Map<String, String> filterChainDefinitionMap) {
        this.filterChainDefinitionMap = filterChainDefinitionMap;
    }

    public Resource[] getLocations() {
        return locations;
    }

    public void setLocations(Resource[] locations) {
        this.locations = locations;
    }

}

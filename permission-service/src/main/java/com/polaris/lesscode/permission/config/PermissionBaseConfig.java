package com.polaris.lesscode.permission.config;

import com.polaris.lesscode.util.JsonUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author roamer
 * @version v1.0
 * @date 2020-11-04 10:11
 */

//@Component
@Deprecated
public class PermissionBaseConfig implements InitializingBean {
    private final PermissionConfigDefinition permissionConfigDefinition;

    private static final Map<Integer, PermissionConfigDefinition.PermissionConfigProperties> CONFIG_MAP = new HashMap<>();

    public PermissionBaseConfig(PermissionConfigDefinition permissionConfigDefinition) {
        this.permissionConfigDefinition = permissionConfigDefinition;
    }

    public static PermissionConfigDefinition.PermissionConfigProperties getPermissionConfigProperties(Integer type) {
        return CONFIG_MAP.get(type);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        permissionConfigDefinition.getConfigs().forEach(d -> {
            try {
                ClassPathResource jsonFile = new ClassPathResource(d.getConfigPath());
                String json = new String(FileCopyUtils.copyToByteArray(jsonFile.getInputStream()), StandardCharsets.UTF_8);
                PermissionConfigDefinition.PermissionConfigProperties definition = JsonUtils.fromJson(json,
                        PermissionConfigDefinition.PermissionConfigProperties.class);
                CONFIG_MAP.put(d.getType(), definition);
            } catch (IOException e) {
                throw new RuntimeException("解析基础权限配置文件失败", e);
            }
        });
    }

}

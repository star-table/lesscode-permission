package com.polaris.lesscode.permission.config;

import com.polaris.lesscode.permission.internal.model.bo.CrmDataAuthOptionInfoBo;
import com.polaris.lesscode.permission.internal.model.bo.FieldAuthOptionInfoBo;
import com.polaris.lesscode.permission.internal.model.bo.OptAuthOptionInfoBo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author roamer
 * @version v1.0
 * @date 2020-11-03 20:59
 */
@Data
//@ConfigurationProperties(prefix = "permission")
//@Configuration
@Deprecated
public class PermissionConfigDefinition {

    private List<ConfigPathDefinition> configs = new ArrayList<>();

    @Data
    public static class ConfigPathDefinition {
        private Integer type;
        private String configPath;
    }

    @Data
    public static class PermissionConfigProperties {
        private Boolean creatable;
        private List<OptAuthOptionInfoBo> optAuthOptions;
        private List<FieldAuthOptionInfoBo> fieldAuthOptions;
        private List<CrmDataAuthOptionInfoBo> crmDataAuthOptions;

        public PermissionConfigProperties() {
            creatable = true;
            optAuthOptions = new ArrayList<>();
            fieldAuthOptions = new ArrayList<>();
            crmDataAuthOptions = new ArrayList<>();
        }
    }

}


package com.polaris.lesscode.permission.model.resp;

import com.polaris.lesscode.permission.internal.model.bo.OptAuthOptionInfoBo;
import lombok.Data;

import java.util.List;

/**
 * 管理组基础配置
 *
 * @author Nico
 * @date 2021/3/11 14:04
 */
@Data
public class ManageGroupBaseConfig {

    /**
     * 操作项可选范围
     *
     * @Author Nico
     * @Date 2021/3/11 14:04
     **/
    private List<OptAuthOptionInfoBo> optAuthOptions;
}

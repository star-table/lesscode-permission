package com.polaris.lesscode.permission.internal.model.req;

import com.polaris.lesscode.permission.internal.model.bo.OptAuthOptionInfoBo;
import lombok.Data;

import java.util.List;

/**
 * 初始化管理组请求结构体
 *
 * @author Nico
 * @date 2020/12/29 16:57
 */
@Data
public class InitDefaultManageGroupReq {

    /**
     * 组织id
     **/
    private Long orgId;

    /**
     * 操作项，为空则不添加，默认为空，使用模板操作项
     **/
    private List<OptAuthOptionInfoBo> authOptions;
}

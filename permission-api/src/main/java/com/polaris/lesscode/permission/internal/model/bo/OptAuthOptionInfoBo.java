package com.polaris.lesscode.permission.internal.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author roamer
 * @version v1.0
 * @date 2020-11-04 16:15
 */
@Data
public class OptAuthOptionInfoBo {
    /**
     * 操作项的code
     */
    private String code;
    /**
     * 操作项的名称
     */
    private String name;
    /**
     * 操作组
     **/
    private String group;

    /**
     * 类型,FuncPermission:功能权限，MenuPermission:菜单权限
     */
    @ApiModelProperty("类型,FuncPermission:功能权限，MenuPermission:菜单权限")
    private String type;
    /**
     * 分组code
     **/
    private String groupCode;
    /**
     * 是否是必选选项
     */
    private Boolean required;
    /**
     * 是否是菜单
     * 只有是菜单的，后选后才会显示对应的表单
     */
    private Boolean isMenu;
    /**
     * 1为启用 2为禁用
     */
    private Integer status;

    /**
     * 默认勾选
     */
    private Boolean defaultSelected;

    public OptAuthOptionInfoBo() {
        required = Boolean.FALSE;
        isMenu = Boolean.FALSE;
        // 默认为1
        status = 1;
    }

    public OptAuthOptionInfoBo(String code, String name, String group) {
        this();
        this.code = code;
        this.name = name;
        this.group = group;
    }

    public Integer getStatus() {
        return status == null ? 1 : status;
    }


    public Boolean getRequired() {
        return required != null && required;
    }

    public Boolean getMenu() {
        return isMenu != null && isMenu;
    }
}

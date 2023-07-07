package com.polaris.lesscode.permission.internal.model.req;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import com.polaris.lesscode.permission.internal.enums.AppPerDefaultGroupLangCode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 应用权限组 entity
 *
 * @author roamer
 * @version v1.0
 * @date 2020-09-01 15:49
 */
@NoArgsConstructor
@Data
public class CreateAppPermissionGroupReq implements Serializable {


    private static final long serialVersionUID = 910959386751737075L;
    /**
     * 主键ID
     **/
    @TableId
    private Long id;

    /**
     * 组织ID
     **/
    @TableField("org_id")
    private Long orgId;

    /**
     * 应用ID
     **/
    @TableField("app_id")
    private Long appId;

    /**
     * lang_code
     * <p>
     * 参照 {@link AppPerDefaultGroupLangCode}
     */
    @TableField("lang_code")
    private String langCode;

    /**
     * 只读 [1: 是, 2: 否]
     **/
    @TableField("read_only")
    private Integer readOnly;

    /**
     * 权限组名称
     **/
    @TableField("name")
    private String name;

    /**
     * 权限组说明
     **/
    @TableField("remake")
    private String remake;

    /**
     * 操作权限 array json
     * <p>
     * 格式：
     * [
     * hasBatchPrint,
     * hasBatchUpdate,
     * hasCopy,
     * hasCreate,
     * hasDelete,
     * hasExport,
     * hasImport,
     * hasRead,
     * hasUpdate
     * ]
     * </p>
     * 选项参照 {@link com.polaris.lesscode.permission.internal.enums.OperateAuthCode}
     */
    @TableField("opt_auth")
    private String optAuth;

    @TableField("table_auth")
    private String tableAuth;

    /**
     * 字段权限 json
     * <p>
     * 格式：
     * {
     * "_sw_1234": 7,
     * "create": 1
     * }
     * </p>
     * <p>
     * 规则：
     * A+B=X <br/>
     * 如：<br/>
     * 可读+可写+脱敏=7 <br/>
     * 可读+可写=5
     * </p>
     * 字段权限参照 {@link com.polaris.lesscode.permission.internal.enums.FormFieldAuthCode}
     */
    @TableField("column_auth")
    private String fieldAuth;

    /**
     * 数据权限 json
     * <p>
     * 格式：
     * <p>
     * {cond: [
     * {
     * type: "user",
     * method: in,
     * value:[],
     * field:"creator",
     * hasCurrentUser:true,
     * hasEmpty:false
     * }
     * ],
     * rel: "and"
     * },
     * </p>
     */
    @TableField("data_auth")
    private String dataAuth;

    /**
     * 视图权限
     **/
    @TableField("view_auth")
    private String viewAuth;

    /**
     * 创建人
     **/
    @TableField("creator")
    private Long creator;

    /**
     * 修改人
     **/
    @TableField("updator")
    private Long updator;

}

package com.polaris.lesscode.permission.internal.model.bo;

import com.polaris.lesscode.permission.internal.enums.ConditionRelationMode;
import com.polaris.lesscode.permission.internal.enums.FieldFilterMethod;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 应用权限组 数据权限数据模型
 * 废弃
 *
 * @author roamer
 * @version v1.0
 * @date 2020-09-01 19:34
 */
@ApiModel("应用权限组 数据权限模型")
@Data
@Deprecated
public class DataAuthBo implements Serializable {

    private static final long serialVersionUID = -2680736376045441110L;

    /**
     * 过滤条件集
     **/
    private List<FilterCondItem> cond;

    /**
     * 条件关联方式
     *
     * <ul>
     *     <li>and：并</li>
     *     <li>or: 或</li>
     * </ul>
     *
     * @see com.polaris.lesscode.permission.internal.enums.ConditionRelationMode
     */
    @ApiModelProperty(value = "条件关联方式", allowableValues = "and, or", required = true)
    private String rel;

    public DataAuthBo() {
        cond = new ArrayList<>();
        rel = ConditionRelationMode.AND.getCode();
    }

    public void appendCond(FilterCondItem filterCondItem) {
        cond.add(filterCondItem);
    }

    /**
     * 过滤条件项
     *
     * @author roamer
     * @version v1.0
     * @date 2020-09-16 11:06
     */
    @Data
    public static class FilterCondItem implements Serializable {

        private static final long serialVersionUID = -6470111582051267111L;
        /**
         * 字段类型
         *
         * @see com.polaris.lesscode.form.internal.enums.FieldTypeEnums#getDataType()
         */
        @ApiModelProperty(value = "字段类型", required = true)
        private String type;
        /**
         * 过滤方法
         *
         * @see FieldFilterMethod
         */
        @ApiModelProperty(value = "过滤方法", required = true)
        private String method;
        /**
         * 值
         **/
        @ApiModelProperty(value = "值", required = true)
        private List<Object> value;
        /**
         * 字段名
         **/
        @ApiModelProperty(value = "字段", required = true)
        private String field;

        /**
         * 是否为当前人员/当前人员所在部门
         **/
        @ApiModelProperty(value = "是否为当前人员/当前人员所在部门")
        private Boolean hasCurrent;

        /**
         * 是否为空
         **/
        @ApiModelProperty(value = "是否为空")
        private Boolean hasEmpty;

        public FilterCondItem() {
            value = new ArrayList<>();
            hasCurrent = false;
            hasEmpty = false;
        }
    }

}

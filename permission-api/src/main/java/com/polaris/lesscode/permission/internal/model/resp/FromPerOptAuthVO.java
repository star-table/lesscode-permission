package com.polaris.lesscode.permission.internal.model.resp;

import com.polaris.lesscode.permission.internal.enums.OperateAuthCode;
import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 应用权限组 操作权限模型（内部调用）
 *
 * @author roamer
 * @version v1.0
 * @date 2020-09-15 13:56
 */
@Data
public class FromPerOptAuthVO implements Serializable {

    private static final long serialVersionUID = 1687674847118257342L;

    private Set<String> permissionSet;

    public FromPerOptAuthVO() {
        permissionSet = new HashSet<>();
    }

    public FromPerOptAuthVO(List<String> permissionList) {
        this();
        permissionSet.addAll(permissionList);
    }

    public boolean hasOpt(String code){
        return permissionSet.contains(code);
    }

    public boolean hasRead() {
        return permissionSet.contains(OperateAuthCode.HAS_READ.getCode());
    }

    public boolean hasCreate() {
        return permissionSet.contains(OperateAuthCode.HAS_CREATE.getCode());
    }

    public boolean hasUpdate() {
        return permissionSet.contains(OperateAuthCode.HAS_UPDATE.getCode());
    }

    public boolean hasDelete() {
        return permissionSet.contains(OperateAuthCode.HAS_DELETE.getCode());
    }

    public boolean hasCopy() {
        return permissionSet.contains(OperateAuthCode.HAS_COPY.getCode());
    }

    public boolean hasImport() {
        return permissionSet.contains(OperateAuthCode.HAS_IMPORT.getCode());
    }

    public boolean hasExport() {
        return permissionSet.contains(OperateAuthCode.HAS_EXPORT.getCode());
    }

    public boolean hasInvite() {
        return permissionSet.contains(OperateAuthCode.HAS_INVITE.getCode());
    }

    public boolean hasShare() {
        return permissionSet.contains(OperateAuthCode.HAS_SHARE.getCode());
    }

    public boolean hasEditMember() {
        return permissionSet.contains(OperateAuthCode.HAS_EDIT_MEMBER.getCode());
    }

    public boolean hasCreateView() {
        return permissionSet.contains(OperateAuthCode.HAS_CREATE_VIEW.getCode());
    }

    @Deprecated
    public boolean hasBatchUpdate() {
        return permissionSet.contains(OperateAuthCode.HAS_BATCH_UPDATE.getCode());
    }

    @Deprecated
    public boolean hasBatchPrint() {
        return permissionSet.contains(OperateAuthCode.HAS_BATCH_PRINT.getCode());
    }


}

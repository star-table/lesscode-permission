package com.polaris.lesscode.permission.internal.model.resp;

import com.polaris.lesscode.dc.internal.dsl.Condition;
import com.polaris.lesscode.permission.internal.enums.AppPerDefaultGroupLangCode;
import com.polaris.lesscode.permission.internal.enums.FormFieldAuthCode;
import com.polaris.lesscode.permission.internal.enums.OperateAuthCode;
import com.polaris.lesscode.permission.internal.model.OrgUserPermissionContext;
import com.polaris.lesscode.permission.internal.model.bo.ViewAuth;
import com.polaris.lesscode.permission.internal.model.resp.FromPerOptAuthVO;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

/**
 * 应用权限
 *
 * @author Nico
 * @date 2021/3/8 15:38
 */
@Data
public class AppAuthorityResp {

    private boolean isSysAdmin;

    private boolean isOrgOwner;

    private boolean isSubAdmin;

    private boolean isOutCollaborator;

    private boolean isCollaborator;

    private AppPerDefaultGroupLangCode langCode;

    private boolean isAppOwner;

    private boolean hasAppRootPermission;

    private Long appId;

    private List<Long> appAuth;

    private Map<String, Map<String, Integer>> fieldAuth;

    private Map<String, ViewAuth> viewAuth;

    private Set<String> optAuth;

    private Set<String> tableAuth;

    private List<Condition> dataAuth;

    private List<AppPerGroupListItem> perGroups;

    public boolean hasOrgRootPermission(){
        return isSysAdmin || isOrgOwner || isSubAdmin;
    }

    public boolean hasAppRootPermission(){
        return isSysAdmin || isOrgOwner
                || (isSubAdmin && (CollectionUtils.isNotEmpty(appAuth) && (appAuth.contains(appId)) || appAuth.contains(-1L)))
                || (langCode != null && langCode.getAdmin());
    }

    public boolean hasRootPermission() {
        return hasOrgRootPermission() || (CollectionUtils.isNotEmpty(appAuth) && appAuth.contains(appId)) || (langCode != null && langCode.getAdmin());
    }

    public boolean hasViewSelectAuth(Long viewId){
        if (hasAppRootPermission()){
            return true;
        }
        ViewAuth auth = viewAuth.get(String.valueOf(viewId));
        if (auth == null){
            return true;
        }
        return auth.isHasSelect();
    }

    public boolean hasViewDeleteAuth(Long viewId){
        if (hasAppRootPermission()){
            return true;
        }
        ViewAuth auth = viewAuth.get(String.valueOf(viewId));
        if (auth == null){
            return true;
        }
        return auth.isHasDelete();
    }

    public boolean hasViewUpdateAuth(Long viewId){
        if (hasAppRootPermission()){
            return true;
        }
        ViewAuth auth = viewAuth.get(String.valueOf(viewId));
        if (auth == null){
            return true;
        }
        return auth.isHasUpdate();
    }

    public boolean hasFieldWriteAuth(Long tableId, String field){
        if (hasAppRootPermission()){
            return true;
        }
        if (fieldAuth.isEmpty()){
            return true;
        }
        Map<String, Integer> fa = fieldAuth.get(tableId.toString());
        if (fa != null) {
            Integer allWrite = fa.get("hasWrite");
            if (allWrite != null && Objects.equals(allWrite, 1)) {
                return true;
            }
            Integer code = fa.get(field);
            if (code == null) {
                return false;
            }
            FormFieldAuthCode auth = FormFieldAuthCode.forValue(code);
            return auth != null && auth.isWrite();
        } else {
            return false;
        }
    }

    public boolean hasFieldReadAuth(Long tableId, String field){
        if (hasAppRootPermission()){
            return true;
        }
        if (fieldAuth.isEmpty()){
            return true;
        }
        Map<String, Integer> fa = fieldAuth.get(tableId.toString());
        if (fa != null) {
            Integer allRead = fa.get("hasRead");
            if (allRead != null && Objects.equals(allRead, 1)) {
                return true;
            }
            Integer code = fa.get(field);
            if (code == null) {
                return false;
            }
            FormFieldAuthCode auth = FormFieldAuthCode.forValue(code);
            return auth != null && auth.isRead();
        } else {
            return false;
        }
    }

    /**
     * 是否要脱敏
     **/
    public boolean hasFieldMaskingAuth(Long tableId, String field){
        if (hasAppRootPermission()){
            return false;
        }
        Map<String, Integer> fa = fieldAuth.get(tableId.toString());
        if (fa != null) {
            Integer code = fa.get(field);
            if (code == null) {
                return false;
            }
            FormFieldAuthCode auth = FormFieldAuthCode.forValue(code);
            return auth == null || auth.isMasking();
        } else {
            return false;
        }
    }

    public boolean hasAppOptAuth(String opt){
        if (hasAppRootPermission()){
            return true;
        }
        return (CollectionUtils.isNotEmpty(optAuth) && optAuth.contains(opt)) || (langCode != null && langCode.getDefaultOptAuth().get().contains(opt));
    }


}

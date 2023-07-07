package com.polaris.lesscode.permission.service.impl;

import com.polaris.lesscode.app.internal.feign.AppPackageProvider;
import com.polaris.lesscode.app.internal.feign.AppProvider;
import com.polaris.lesscode.app.internal.resp.AppPackageResp;
import com.polaris.lesscode.app.internal.resp.AppResp;
import com.polaris.lesscode.form.internal.feign.AppFormProvider;
import com.polaris.lesscode.form.internal.resp.AppFormFilter;
import com.polaris.lesscode.form.internal.resp.AppFormResp;
import com.polaris.lesscode.permission.constant.PermissionConstant;
import com.polaris.lesscode.permission.service.AppService;
import com.polaris.lesscode.vo.Result;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author roamer
 * @version v1.0
 * @date 2020-11-16 16:38
 */
@Service
public class AppServiceImpl implements AppService {
    private final AppProvider appProvider;
    private final AppPackageProvider appPackageProvider;
    private final AppFormProvider appFormProvider;

    public AppServiceImpl(AppProvider appProvider, AppPackageProvider appPackageProvider, AppFormProvider appFormProvider) {
        this.appProvider = appProvider;
        this.appPackageProvider = appPackageProvider;
        this.appFormProvider = appFormProvider;
    }

    @Override
    public List<AppResp> getAppList(Long orgId) {
        Result<List<AppResp>> result = appProvider.getAppList(orgId, null, null);
        return result.getData();
    }

    @Override
    public List<AppResp> getAppList(Long orgId, Collection<Long> appIds) {
        Result<List<AppResp>> result = appProvider.getAppInfoList(orgId, appIds);
        return result.getData();
    }

    @Override
    public Map<Long, AppResp> getAppMap(Long orgId) {
        return getAppList(orgId).stream().collect(Collectors.toMap(AppResp::getId, v -> v));
    }

    @Override
    public AppResp getApp(Long orgId, Long appId) {
        return appProvider.getAppInfo(orgId, appId).getData();
    }

    @Override
    public List<AppPackageResp> getPkgList(Long orgId) {
        Result<List<AppPackageResp>> result = appPackageProvider.getAppPkgList(orgId);
        return result.getData();
    }

    @Override
    public Map<Long, AppPackageResp> getPkgMap(Long orgId) {
        return getPkgList(orgId).stream().collect(Collectors.toMap(AppPackageResp::getId, v -> v));
    }

//    @Override
//    public AppFormResp getTablesByApp(Long orgId, Long appId) {
//        return appFormProvider.getFormByAppId(orgId, appId).getData();
//    }
//
//    @Override
//    public List<AppFormResp> getTablesByAppIds(Long orgId, Collection<Long> appIdList) {
//        AppFormFilter formFilter = new AppFormFilter();
//        formFilter.setOrgId(orgId);
//        formFilter.setAppIds(new ArrayList<>(appIdList));
//        formFilter.setDelFlag(PermissionConstant.NO);
//        return appFormProvider.filter(formFilter).getData();
//    }
//
//    @Override
//    public String getFormFieldConfigByApp(Long orgId, Long appId) {
//        AppFormResp appFormResp = getFormByApp(orgId, appId);
//        if (Objects.isNull(appFormResp)) {
//            return null;
//        }
//        return appFormResp.getConfig();
//    }
//
//    @Override
//    public Map<Long, String> getFormFieldConfigMapByAppIds(Long orgId, Collection<Long> appIdList) {
//        List<AppFormResp> appFormRespList = getFormListByAppIds(orgId, appIdList);
//        if (appFormRespList.isEmpty()) {
//            return null;
//        }
//        return appFormRespList.stream().collect(Collectors.toMap(AppFormResp::getAppId, AppFormResp::getConfig));
//    }
}

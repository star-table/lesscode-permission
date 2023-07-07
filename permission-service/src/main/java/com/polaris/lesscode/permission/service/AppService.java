package com.polaris.lesscode.permission.service;

import com.polaris.lesscode.app.internal.resp.AppPackageResp;
import com.polaris.lesscode.app.internal.resp.AppResp;
import com.polaris.lesscode.form.internal.resp.AppFormResp;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author roamer
 * @version v1.0
 * @date 2020-11-16 16:35
 */
public interface AppService {

    List<AppResp> getAppList(Long orgId);

    List<AppResp> getAppList(Long orgId, Collection<Long> appIds);

    Map<Long, AppResp> getAppMap(Long orgId);

    AppResp getApp(Long orgId, Long appId);

    List<AppPackageResp> getPkgList(Long orgId);

    Map<Long, AppPackageResp> getPkgMap(Long orgId);
}

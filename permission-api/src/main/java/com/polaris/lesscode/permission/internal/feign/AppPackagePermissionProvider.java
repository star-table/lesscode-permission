/**
 *
 */
package com.polaris.lesscode.permission.internal.feign;

import com.polaris.lesscode.consts.ApplicationConsts;
import com.polaris.lesscode.permission.internal.api.AppPackagePermissionApi;
import com.polaris.lesscode.permission.internal.fallback.AppPkgPermissionFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author admin
 *
 */
@FeignClient(value = ApplicationConsts.APPLICATION_PERMISSION, fallbackFactory = AppPkgPermissionFallbackFactory.class, contextId = "appPackagePermissionProvider")
public interface AppPackagePermissionProvider extends AppPackagePermissionApi {

}

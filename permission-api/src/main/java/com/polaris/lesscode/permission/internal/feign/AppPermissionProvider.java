/**
 * 
 */
package com.polaris.lesscode.permission.internal.feign;

import com.polaris.lesscode.consts.ApplicationConsts;
import com.polaris.lesscode.permission.internal.api.AppPermissionApi;
import com.polaris.lesscode.permission.internal.fallback.AppPermissionFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author admin
 *
 */
@FeignClient( value = ApplicationConsts.APPLICATION_PERMISSION, fallbackFactory = AppPermissionFallbackFactory.class,contextId = "appPermissionProvider")
public interface AppPermissionProvider extends AppPermissionApi {

}

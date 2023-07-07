/**
 *
 */
package com.polaris.lesscode.permission.internal.feign;

import com.polaris.lesscode.consts.ApplicationConsts;
import com.polaris.lesscode.permission.internal.api.PermissionApi;
import com.polaris.lesscode.permission.internal.fallback.PermissionFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Bomb.
 *
 */
@FeignClient(value = ApplicationConsts.APPLICATION_PERMISSION, fallbackFactory = PermissionFallbackFactory.class, contextId = "permissionProvider")
public interface PermissionProvider extends PermissionApi {

}

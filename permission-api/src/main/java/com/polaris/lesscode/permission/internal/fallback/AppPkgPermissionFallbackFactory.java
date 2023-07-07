/**
 * 
 */
package com.polaris.lesscode.permission.internal.fallback;

import com.polaris.lesscode.consts.ApplicationConsts;
import com.polaris.lesscode.feign.AbstractBaseFallback;
import com.polaris.lesscode.permission.internal.api.AppPackagePermissionApi;
import com.polaris.lesscode.permission.internal.model.req.DeleteAppPackagePermissionReq;
import com.polaris.lesscode.permission.internal.model.req.ModifyAppPackagePermissionReq;
import com.polaris.lesscode.permission.internal.model.resp.SimpleAppPermissionResp;
import com.polaris.lesscode.vo.Result;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author admin
 *
 */
@Component
public class AppPkgPermissionFallbackFactory extends AbstractBaseFallback implements FallbackFactory<AppPackagePermissionApi> {

	@Override
	public AppPackagePermissionApi create(Throwable cause) {
		return new AppPackagePermissionApi() {
			
			@Override
			public Result<Boolean> saveOrUpdateAppPackagePermission(ModifyAppPackagePermissionReq req) {
				return wrappDeal(ApplicationConsts.APPLICATION_PERMISSION,cause,()->{
					return Result.ok(Boolean.TRUE);
				});
			}
			
			@Override
			public Result<SimpleAppPermissionResp> getSimpleAppPackagePermission(Long orgId, Long appPackageId) {
				return wrappDeal(ApplicationConsts.APPLICATION_PERMISSION,cause,()->{
					return Result.ok(new SimpleAppPermissionResp());
				});
			}

			@Override
			public Result<Boolean> deleteAppPackagePermission(DeleteAppPackagePermissionReq req) {
				return wrappDeal(ApplicationConsts.APPLICATION_PERMISSION,cause,()->{
					return Result.ok(Boolean.TRUE);
				});
			}
		};
	}

}

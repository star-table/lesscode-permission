/**
 * 
 */
package com.polaris.lesscode.permission.internal.fallback;

import com.polaris.lesscode.consts.ApplicationConsts;
import com.polaris.lesscode.feign.AbstractBaseFallback;
import com.polaris.lesscode.permission.internal.api.PermissionApi;
import com.polaris.lesscode.permission.internal.model.UserPermissionVO;
import com.polaris.lesscode.permission.internal.model.resp.UserAppPermissionListResp;
import com.polaris.lesscode.vo.Result;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author Bomb.
 *
 */
@Component
public class PermissionFallbackFactory extends AbstractBaseFallback implements FallbackFactory<PermissionApi> {

	@Override
	public PermissionApi create(Throwable cause) {
		
		return new PermissionApi() {			
			@Override
			public Result<UserPermissionVO> getUserPermission(Long orgId, Long userId) {
				return wrappDeal(ApplicationConsts.APPLICATION_PERMISSION,cause,()->{
					return Result.ok(new UserPermissionVO());
				});
			}

			@Override
			public Result<UserAppPermissionListResp> getUserHavePerList(Long orgId, Long userId) {
				return wrappDeal(ApplicationConsts.APPLICATION_PERMISSION,cause,()->{
					return Result.ok(new UserAppPermissionListResp());
				});
			}

        };
	}



}

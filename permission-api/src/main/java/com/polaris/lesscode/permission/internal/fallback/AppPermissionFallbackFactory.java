/**
 * 
 */
package com.polaris.lesscode.permission.internal.fallback;

import com.polaris.lesscode.consts.ApplicationConsts;
import com.polaris.lesscode.dc.internal.dsl.Condition;
import com.polaris.lesscode.feign.AbstractBaseFallback;
import com.polaris.lesscode.permission.internal.api.AppPermissionApi;
import com.polaris.lesscode.permission.internal.model.bo.ViewAuth;
import com.polaris.lesscode.permission.internal.model.req.*;
import com.polaris.lesscode.permission.internal.model.resp.*;
import com.polaris.lesscode.vo.Result;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author Bomb.
 *
 */
@Component
public class AppPermissionFallbackFactory extends AbstractBaseFallback implements FallbackFactory<AppPermissionApi> {

	@Override
	public AppPermissionApi create(Throwable cause) {
		return new AppPermissionApi() {

			@Override
			public Result<List<AppPerGroupListItem>> getAppPermissionGroupList(Long orgId, Long appId) {
				return wrappDeal(ApplicationConsts.APPLICATION_PERMISSION,cause,()->{
					return Result.ok(new ArrayList<>());
				});
			}

			@Override
			public Result<List<AppPermissionGroupResp>> getAppPermissionGroupBatch(List<Long> appIds) {
				return wrappDeal(ApplicationConsts.APPLICATION_PERMISSION,cause,()->{
					return Result.ok(new ArrayList<>());
				});
			}

			@Override
			public Result<List<AppPermissionGroupResp>> createAppPermissionGroup(List<CreateAppPermissionGroupReq> req) {
				return wrappDeal(ApplicationConsts.APPLICATION_PERMISSION,cause,()->{
					return Result.ok(new ArrayList());
				});
			}

			@Override
			public Result<Boolean> initAppPermission(InitAppPermissionReq req) {
				return wrappDeal(ApplicationConsts.APPLICATION_PERMISSION,cause,()->{
					return Result.ok(Boolean.TRUE);
				});
			}

			@Override
			public Result<Boolean> initAppPermissionFieldAuthCreateTable(InitAppPermissionFieldAuthCreateTableReq req) {
				return wrappDeal(ApplicationConsts.APPLICATION_PERMISSION,cause,()->{
					return Result.ok(Boolean.TRUE);
				});
			}

			@Override
			public Result<Boolean> initAppPermissionFieldAuthDeleteTable(InitAppPermissionFieldAuthDeleteTableReq req) {
				return wrappDeal(ApplicationConsts.APPLICATION_PERMISSION,cause,()->{
					return Result.ok(Boolean.TRUE);
				});
			}

//			@Override
//			public Result<Boolean> updateFormFieldConfig(UpdateFieldConfigReq req) {
//				return wrappDeal(ApplicationConsts.APPLICATION_PERMISSION,cause,()->{
//					return Result.ok(Boolean.TRUE);
//				});
//			}

			@Override
			public Result<Boolean> updateAppPermission(UpdateAppPermissionReq req) {
				return wrappDeal(ApplicationConsts.APPLICATION_PERMISSION,cause,()->{
					return Result.ok(Boolean.TRUE);
				});
			}

			@Override
			public Result<Boolean> updateAppPermissionByLangCode(UpdateAppPermissionByLangCodeReq req) {
				return wrappDeal(ApplicationConsts.APPLICATION_PERMISSION,cause,()->{
					return Result.ok(Boolean.TRUE);
				});
			}

			@Override
			public Result<Boolean> deleteAppPermission(DeleteAppPermissionReq req) {
				return wrappDeal(ApplicationConsts.APPLICATION_PERMISSION,cause,()->{
					return Result.ok(Boolean.TRUE);
				});
			}

			@Override
			public Result<FromPerOptAuthVO> getOptAuth(Long orgId, Long appId, Long userId) {
				return wrappDeal(ApplicationConsts.APPLICATION_PERMISSION, cause, () -> {
					return Result.ok(new FromPerOptAuthVO(new ArrayList<String>()));
				});
			}

			@Override
			public Result<List<String>> getOptAuthList(Long orgId, Long appId, Long userId) {
				return wrappDeal(ApplicationConsts.APPLICATION_PERMISSION, cause, () -> {
					return Result.ok(Collections.emptyList());
				});
			}

			@Override
			public Result<Map<String, Map<String, Integer>>> getFieldAuth(Long orgId, Long appId, Long userId) {
				return wrappDeal(ApplicationConsts.APPLICATION_PERMISSION, cause, () -> {
					return Result.ok(new HashMap<String, Map<String, Integer>>());
				});
			}

			@Override
			public Result<List<String>> getTableAuth(Long orgId, Long appId, Long userId) {
				return wrappDeal(ApplicationConsts.APPLICATION_PERMISSION, cause, () -> {
					return Result.ok(new ArrayList<>());
				});
			}

			@Override
			public Result<Map<String, ViewAuth>> getViewAuth(Long orgId, Long appId, Long userId) {
				return wrappDeal(ApplicationConsts.APPLICATION_PERMISSION,cause,()->{
					return Result.ok(new LinkedHashMap<>());
				});
			}

			@Override
			public Result<List<Condition>> getDataAuth(Long orgId, Long appId, Long userId) {
				return wrappDeal(ApplicationConsts.APPLICATION_PERMISSION, cause, () -> {
					return Result.ok(new ArrayList<Condition>());
				});
			}

			@Override
			public Result<AppAuthorityResp> getAppAuthority(Long orgId, Long appId, Long tableId, Long userId) {
				return wrappDeal(ApplicationConsts.APPLICATION_PERMISSION, cause, () -> {
					return Result.ok(new AppAuthorityResp());
				});
			}

			@Override
			public Result<AppAuthorityResp> getAppAuthorityByCollaboratorRoles(Long orgId, Long appId, Long userId, List<Long> collaboratorRoleIds) {
				return wrappDeal(ApplicationConsts.APPLICATION_PERMISSION, cause, () -> {
					return Result.ok(new AppAuthorityResp());
				});
			}

			@Override
			public Result<AppAuthorityResp> getAppAuthorityWithoutCollaborator(Long orgId, Long appId, Long userId) {
				return wrappDeal(ApplicationConsts.APPLICATION_PERMISSION, cause, () -> {
					return Result.ok(new AppAuthorityResp());
				});
			}

			@Override
			public Result<Map<Long, AppAuthorityResp>> getAppAuthorityBatch(GetAppAuthorityBatchReq req) {
				return wrappDeal(ApplicationConsts.APPLICATION_PERMISSION, cause, () -> {
					return Result.ok(new HashMap<>());
				});
			}

			@Override
			public Result<AppAuthorityResp> getDataAuthority(Long orgId, Long appId, Long userId, Long dataId) {
				return wrappDeal(ApplicationConsts.APPLICATION_PERMISSION, cause, () -> {
					return Result.ok(new AppAuthorityResp());
				});
			}

			@Override
			public Result<Map<Long, AppAuthorityResp>> getDataAuthorityBatch(GetDataAuthorityBatchReq getDataAuthorityBatchReq) {
				return wrappDeal(ApplicationConsts.APPLICATION_PERMISSION, cause, () -> {
					return Result.ok(new HashMap<>());
				});
			}

			@Override
			public Result<MemberGroupMappingsResp> memberGroupMappings(Long orgId, Long appId, Long userId) {
				return wrappDeal(ApplicationConsts.APPLICATION_PERMISSION, cause, () -> {
					return Result.ok(new MemberGroupMappingsResp());
				});
			}

			@Override
			public Result<Boolean> removeAppMembers(RemoveAppMembersReq req) {
				return wrappDeal(ApplicationConsts.APPLICATION_PERMISSION, cause, () -> {
					return Result.ok(true);
				});
			}

			@Override
			public Result<Boolean> addAppMembers(AddAppMembersReq req) {
				return wrappDeal(ApplicationConsts.APPLICATION_PERMISSION, cause, () -> {
					return Result.ok(true);
				});
			}

			@Override
			public Result<Boolean> copyPermissionGroup(CopyAppPermissionGroupReq req) {
				return wrappDeal(ApplicationConsts.APPLICATION_PERMISSION, cause, () -> {
					return Result.ok(true);
				});
			}

			@Override
			public Result<Boolean> updatePermissionTime(UpdatePermissionTimeReq req) {
				return wrappDeal(ApplicationConsts.APPLICATION_PERMISSION, cause, () -> {
					return Result.ok(true);
				});
			}

			@Override
			public Result<GetPermissionUpdateTimeResp> getPermissionUpdateTime(GetPermissionUpdateTimeReq req) {
				return wrappDeal(ApplicationConsts.APPLICATION_PERMISSION, cause, () -> {
					return Result.ok(new GetPermissionUpdateTimeResp());
				});
			}
		};
	}

	

}

package com.polaris.lesscode.permission.internal.model.req;

import lombok.Data;

@Data
public class CopyAppPermissionGroupReq {

    private Long sourceAppId;

    private Long targetAppId;
}

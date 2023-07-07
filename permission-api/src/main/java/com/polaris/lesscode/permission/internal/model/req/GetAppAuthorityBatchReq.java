package com.polaris.lesscode.permission.internal.model.req;

import lombok.Data;

import java.util.List;

@Data
public class GetAppAuthorityBatchReq {

    private Long orgId;

    private Long userId;

    private List<Long> appIds;
}

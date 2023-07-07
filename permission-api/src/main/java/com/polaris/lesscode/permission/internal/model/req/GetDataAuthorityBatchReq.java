package com.polaris.lesscode.permission.internal.model.req;

import lombok.Data;

import java.util.List;

@Data
public class GetDataAuthorityBatchReq {

    private Long orgId;

    private Long appId;

    private Long userId;

    private List<Long> dataIds;

}

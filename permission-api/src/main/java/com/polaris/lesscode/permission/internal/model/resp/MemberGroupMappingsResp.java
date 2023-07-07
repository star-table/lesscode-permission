package com.polaris.lesscode.permission.internal.model.resp;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class MemberGroupMappingsResp {

    private Map<Long, List<AppPerGroupListItem>> userGroupMappings;

    private Map<Long, List<AppPerGroupListItem>> deptGroupMappings;

    private Map<Long, List<AppPerGroupListItem>> roleGroupMappings;
}

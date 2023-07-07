package com.polaris.lesscode.permission.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.polaris.lesscode.permission.entity.PerAppPermissionGroup;
import lombok.Data;

@Data
public class PerGroupEvent {
    @JsonProperty("orgId")
    private Long orgId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonProperty("appId")
    private Long appId;

    @JsonProperty("projectId")
    private Long projectId;

    @JsonProperty("userId")
    private Long userId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonProperty("groupId")
    private Long groupId;

    @JsonProperty("new")
    private Object newData;
}

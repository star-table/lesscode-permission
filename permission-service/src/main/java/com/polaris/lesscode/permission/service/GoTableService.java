package com.polaris.lesscode.permission.service;

import com.polaris.lesscode.form.internal.sula.FieldParam;
import com.polaris.lesscode.gotable.internal.req.CreateSummeryTableRequest;
import com.polaris.lesscode.gotable.internal.req.CreateTableRequest;
import com.polaris.lesscode.gotable.internal.req.ReadSummeryTableIdRequest;
import com.polaris.lesscode.gotable.internal.resp.CreateSummeryTableResp;
import com.polaris.lesscode.gotable.internal.resp.CreateTableResp;
import com.polaris.lesscode.gotable.internal.resp.ReadSummeryTableIdResp;
import com.polaris.lesscode.gotable.internal.resp.TableSchemas;

import java.util.List;
import java.util.Map;

public interface GoTableService {
    List<TableSchemas> readTableSchemasByAppId(Long orgId, Long appId, Long userId);
    Map<String, FieldParam> readSchemaByAppId(Long appId, Long orgId, Long tableId, Long userId);
    ReadSummeryTableIdResp readSummeryTableId(ReadSummeryTableIdRequest req, Long orgId, Long userId);
    List<String> getUserAppCollaboratorRoles(Long orgId, Long userId, Long appId);
}

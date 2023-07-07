package com.polaris.lesscode.permission.service;

import com.alibaba.fastjson.JSON;
import com.polaris.lesscode.form.internal.sula.FieldParam;
import com.polaris.lesscode.gotable.internal.api.GoTableApi;
import com.polaris.lesscode.gotable.internal.req.*;
import com.polaris.lesscode.gotable.internal.resp.CreateSummeryTableResp;
import com.polaris.lesscode.gotable.internal.resp.CreateTableResp;
import com.polaris.lesscode.gotable.internal.resp.ReadSummeryTableIdResp;
import com.polaris.lesscode.gotable.internal.resp.TableSchemas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoTableServiceImpl implements GoTableService{
    @Autowired
    private GoTableApi goTableApi;

    public List<TableSchemas> readTableSchemasByAppId(Long orgId, Long appId, Long userId) {
        return goTableApi.readSchemasByAppId(
                new ReadTableSchemasByAppIdRequest(appId, false), orgId.toString(), userId.toString()).getTables();
    }

    // 理论上来说form只有汇总表的时候用到，一般都是通过tableId来获取
    public Map<String, FieldParam> readSchemaByAppId(Long appId, Long orgId, Long tableId, Long userId) {
        TableSchemas schemas = null;
        if (tableId != null && !tableId.equals(0L)) {
            schemas = readSchema(tableId,orgId,userId);
        } else {
            List<TableSchemas> list = goTableApi.readSchemasByAppId(new ReadTableSchemasByAppIdRequest(appId,false), orgId.toString(),userId.toString()).getTables();
            if (list != null && list.size() >= 1) {
                schemas = list.get(0);
            }
        }

        if (null != schemas) {
            Map<String, FieldParam> fieldParams =  new LinkedHashMap<>();
            if (schemas.getColumns() != null) {
                schemas.getColumns().forEach(f -> {
                    FieldParam fp  = JSON.toJavaObject(f,FieldParam.class);
                    fieldParams.put(fp.getName(), fp);
                });
            }
            return fieldParams;
        }

        return null;
    }

    public List<TableSchemas> readSchemas(List<Long> tableIds, Long orgId, Long userId) {
        return goTableApi.readSchemas(new ReadTableSchemasRequest(tableIds,true, true), orgId.toString(),userId.toString()).getTables();
    }

    public TableSchemas readSchema(Long tableId, Long orgId, Long userId) {
        List<Long> tableIds = new ArrayList<>();
        tableIds.add(tableId);
        List<TableSchemas> list = readSchemas(tableIds, orgId,userId);
        return list.get(0);
    }

    public ReadSummeryTableIdResp readSummeryTableId(ReadSummeryTableIdRequest req, Long orgId, Long userId) {
        return goTableApi.readSummeryTableId(req,orgId.toString(),userId.toString());
    }

    public List<String> getUserAppCollaboratorRoles(Long orgId, Long userId, Long appId) {
        return goTableApi.getUserAppCollaboratorRoles(new GetUserAppCollaboratorRolesRequest(appId, userId), orgId.toString(), userId.toString()).getRoleIds();
    }
}

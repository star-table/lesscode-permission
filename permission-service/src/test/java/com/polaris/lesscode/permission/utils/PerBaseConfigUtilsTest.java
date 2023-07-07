package com.polaris.lesscode.permission.utils;

import com.polaris.lesscode.permission.internal.model.bo.OptAuthOptionInfoBo;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author roamer
 * @version v1.0
 * @date 2021/2/22 11:21
 */
public class PerBaseConfigUtilsTest {

    @Test
    public void jsonToOptAuthOptionInfoBoList() {
        List<OptAuthOptionInfoBo> a = PerBaseConfigUtils.jsonToOptAuthOptionInfoBoList("[{\"code\": \"hasCreateView\", \"name\": \"创建视图\", \"isMenu\": false, \"required\": false}, {\"code\": \"hasRead\", \"name\": \"查看\", \"isMenu\": true, \"required\": true}, {\"code\": \"hasCreate\", \"name\": \"添加\"}, {\"code\": \"hasCopy\", \"name\": \"复制\"}, {\"code\": \"hasUpdate\", \"name\": \"编辑\"}, {\"code\": \"hasDelete\", \"name\": \"删除\"}, {\"code\": \"hasImport\", \"name\": \"导入\"}, {\"code\": \"hasExport\", \"name\": \"导出\"}, {\"code\": \"hasInvite\", \"name\": \"邀请\"}, {\"code\": \"hasShare\", \"name\": \"分享\"}, {\"code\": \"hasEditMember\", \"name\": \"编辑成员\"}]");
        Assert.assertEquals(a.size(), 11);

        Set<String> menus = a.stream().filter(OptAuthOptionInfoBo::getIsMenu).map(OptAuthOptionInfoBo::getCode).collect(Collectors.toSet());
        Assert.assertEquals(menus.size(), 1);
    }
}
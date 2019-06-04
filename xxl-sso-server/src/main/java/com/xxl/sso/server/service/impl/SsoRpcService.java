package com.xxl.sso.server.service.impl;

import com.xxl.sso.core.rpc.ISsoRpcService;
import com.xxl.sso.core.user.XxlSsoUser;
import com.xxl.sso.server.store.SsoLoginStore;
import com.xxl.sso.server.store.SsoSessionIdHelper;
import org.springframework.stereotype.Service;

/**
 * Copyright (C), 2018, 银联智惠信息服务（上海）有限公司
 *
 * @author mxh
 * @version 0.0.1
 * @date 2019-06-03 11:24:00
 * @Description
 * @throws
 */
@Service
public class SsoRpcService implements ISsoRpcService {

    @Override
    public XxlSsoUser get(String storeKey) {
        return SsoLoginStore.get(storeKey);
    }

    @Override
    public void remove(String storeKey) {
        SsoLoginStore.remove(storeKey);
    }

    @Override
    public void put(String storeKey, XxlSsoUser xxlUser) {
        SsoLoginStore.put(storeKey, xxlUser);
    }

    @Override
    public String parseStoreKey(String sessionId) {
        return SsoSessionIdHelper.parseStoreKey(sessionId);
    }

    @Override
    public String parseVersion(String sessionId) {
        return SsoSessionIdHelper.parseVersion(sessionId);
    }
}

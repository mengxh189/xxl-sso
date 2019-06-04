package com.xxl.sso.core.rpc;

import com.xxl.sso.core.user.XxlSsoUser;

/**
 * Copyright (C), 2017, 银联智惠信息服务（上海）有限公司
 *
 * @author mxh
 * @version 0.0.1
 * @date 2019-06-03 11:23:00
 * @Description
 * @throws
 */
public interface ISsoRpcService {

    XxlSsoUser get(String storeKey);

    void remove(String storeKey);

    void put(String storeKey, XxlSsoUser xxlUser);

    String parseStoreKey(String sessionId);

    String parseVersion(String sessionId);
}

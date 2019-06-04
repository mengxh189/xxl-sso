package com.xxl.sso.core.cache;

import com.xxl.sso.core.conf.GlobalConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (C), 2019, 银联智惠信息服务（上海）有限公司
 *
 * @author mxh
 * @version 0.0.1
 * @date 2019-06-03 16:51:00
 * @Description
 * @throws
 */
public class CacheManager {

    private static Map<String, String> cacheMap = new HashMap<>();

    public static void put(String url) {
        cacheMap.put(GlobalConfig.SSO_SERVER_RUL, url);
    }

    public static String get() {
        return cacheMap.get(GlobalConfig.SSO_SERVER_RUL);
    }
}

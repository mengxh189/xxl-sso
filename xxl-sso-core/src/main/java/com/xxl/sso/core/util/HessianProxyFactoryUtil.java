package com.xxl.sso.core.util;

import com.caucho.hessian.client.HessianProxyFactory;

import java.net.MalformedURLException;

/**
 * Copyright (C), 2018, 银联智惠信息服务（上海）有限公司
 *
 * @author mxh
 * @version 0.0.1
 * @date 2019-06-03 11:35:00
 * @Description
 * @throws
 */
public class HessianProxyFactoryUtil {

    public static <T> T getHessianBean(Class<T> clazz, String url) throws MalformedURLException {
        HessianProxyFactory factory = new HessianProxyFactory();
        factory.setOverloadEnabled(true);
        return (T) factory.create(clazz, url);
    }
}

package com.xxl.sso.server.rpc;

import com.xxl.sso.core.rpc.ISsoRpcService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.caucho.HessianServiceExporter;

import javax.annotation.Resource;

/**
 * Copyright (C), 2018, 银联智惠信息服务（上海）有限公司
 *
 * @author mxh
 * @version 0.0.1
 * @date 2019-06-03 11:18:00
 * @Description
 * @throws
 */
@Configuration
public class HessianRpcConfig {

    @Resource
    private ISsoRpcService ssoRpcService;

    @Bean("/sso")
    public HessianServiceExporter serviceExporter() {
        HessianServiceExporter exporter = new HessianServiceExporter();
        exporter.setServiceInterface(ISsoRpcService.class);
        exporter.setService(ssoRpcService);
        return exporter;
    }
}

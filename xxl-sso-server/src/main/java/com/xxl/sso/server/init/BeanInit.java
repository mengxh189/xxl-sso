package com.xxl.sso.server.init;

/**
 * Copyright (C), 2018, 银联智惠信息服务（上海）有限公司
 *
 * @author mxh
 * @version 0.0.1
 * @date 2019-06-03 17:45:00
 * @Description
 * @throws
 */
//@Configuration
public class BeanInit {

    /*@Bean
    public FilterRegistrationBean xxlSsoWebFilterRegistration() {

        FilterRegistrationBean registration = new FilterRegistrationBean();

        registration.setName("XxlSsoWebFilter");
        registration.setOrder(1);
        registration.addUrlPatterns("/*");
        registration.setFilter(new XxlSsoWebFilter());
        registration.addInitParameter(Conf.SSO_SERVER, "http://localhost:8090/hessianService");
        registration.addInitParameter(Conf.SSO_LOGOUT_PATH, "/hessianService/logout");
        registration.addInitParameter(Conf.SSO_EXCLUDED_PATHS, "");

        return registration;
    }*/

//    @Bean
//    public ServletRegistrationBean dispatcherRegistration(DispatcherServlet dispatcherServlet) {
//        return new ServletRegistrationBean(dispatcherServlet, "/sso/*");
//    }

    /*@Bean
    public FilterRegistrationBean xxlSsoTokenFilterRegistration() {

        FilterRegistrationBean registration = new FilterRegistrationBean();

        registration.setName("XxlSsoTokenFilter");
        registration.setOrder(1);
        registration.addUrlPatterns("/*");
        registration.setFilter(new XxlSsoTokenFilter());
        registration.addInitParameter(Conf.SSO_SERVER, "http://127.0.0.1:8090");
        registration.addInitParameter(Conf.SSO_LOGOUT_PATH, "/logout");
        registration.addInitParameter(Conf.SSO_EXCLUDED_PATHS, "");

        return registration;
    }*/
}

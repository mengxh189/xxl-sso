package com.xxl.sso.core.login;

import com.xxl.sso.core.cache.CacheManager;
import com.xxl.sso.core.conf.Conf;
import com.xxl.sso.core.rpc.ISsoRpcService;
import com.xxl.sso.core.user.XxlSsoUser;
import com.xxl.sso.core.util.HessianProxyFactoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;


public class SsoTokenLoginHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(SsoTokenLoginHelper.class);

    private static final String SSO_SERVER_URL = "http://192.168.87.33:8090/hessianService";
    /**
     * client login
     *
     * @param sessionId
     * @param xxlUser
     */
    public static void login(String sessionId, XxlSsoUser xxlUser) {

        ISsoRpcService ssoRpcService = null;
        try {
            ssoRpcService = HessianProxyFactoryUtil.getHessianBean(ISsoRpcService.class, CacheManager.get());
//            ssoRpcService = HessianProxyFactoryUtil.getHessianBean(ISsoRpcService.class, SSO_SERVER_URL);
        } catch (MalformedURLException e) {
            LOGGER.error("connect sso server failed, execption message:{}", e);
        }

        String storeKey = ssoRpcService.parseStoreKey(sessionId);
        if (storeKey == null) {
            throw new RuntimeException("parseStoreKey Fail, sessionId:" + sessionId);
        }

        ssoRpcService.put(storeKey, xxlUser);
    }

    /**
     * client logout
     *
     * @param sessionId
     */
    public static void logout(String sessionId) {

        ISsoRpcService ssoRpcService = null;
        try {
            ssoRpcService = HessianProxyFactoryUtil.getHessianBean(ISsoRpcService.class, CacheManager.get());
//            ssoRpcService = HessianProxyFactoryUtil.getHessianBean(ISsoRpcService.class, SSO_SERVER_URL);
        } catch (MalformedURLException e) {
            LOGGER.error("connect sso server failed, execption message:{}", e);
        }
        String storeKey = null;
        storeKey = ssoRpcService.parseStoreKey(sessionId);
        if (storeKey == null) {
            return;
        }

        ssoRpcService.remove(storeKey);
    }

    /**
     * client logout
     *
     * @param request
     */
    public static void logout(HttpServletRequest request) {
        String headerSessionId = request.getHeader(Conf.SSO_SESSIONID);
        logout(headerSessionId);
    }


    /**
     * login check
     *
     * @param sessionId
     * @return
     */
    public static XxlSsoUser loginCheck(String sessionId) {

        String storeKey = null;
        ISsoRpcService ssoRpcService = null;
        try {
            String url = CacheManager.get();
            LOGGER.info("sso server url:{}", url);
            ssoRpcService = HessianProxyFactoryUtil.getHessianBean(ISsoRpcService.class, url);
//            ssoRpcService = HessianProxyFactoryUtil.getHessianBean(ISsoRpcService.class, SSO_SERVER_URL);
        } catch (MalformedURLException e) {
            LOGGER.error("connect sso server failed, execption message:{}", e);
        }
        storeKey = ssoRpcService.parseStoreKey(sessionId);
        if (storeKey == null) {
            return null;
        }

        XxlSsoUser xxlUser = null;
        xxlUser = ssoRpcService.get(storeKey);
        if (xxlUser != null) {
            String version = null;
            version = ssoRpcService.parseVersion(sessionId);
            if (xxlUser.getVersion().equals(version)) {

                // After the expiration time has passed half, Auto refresh
                if ((System.currentTimeMillis() - xxlUser.getExpireFreshTime()) > xxlUser.getExpireMinite() / 2) {
                    xxlUser.setExpireFreshTime(System.currentTimeMillis());
                    ssoRpcService.put(storeKey, xxlUser);
                }

                return xxlUser;
            }
        }
        return null;
    }


    /**
     * login check
     *
     * @param request
     * @return
     */
    public static XxlSsoUser loginCheck(HttpServletRequest request) {
        String headerSessionId = request.getHeader(Conf.SSO_SESSIONID);
        return loginCheck(headerSessionId);
    }
}

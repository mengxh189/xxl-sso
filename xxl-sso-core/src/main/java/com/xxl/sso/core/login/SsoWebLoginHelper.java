package com.xxl.sso.core.login;

import com.xxl.sso.core.cache.CacheManager;
import com.xxl.sso.core.conf.Conf;
import com.xxl.sso.core.rpc.ISsoRpcService;
import com.xxl.sso.core.user.XxlSsoUser;
import com.xxl.sso.core.util.CookieUtil;
import com.xxl.sso.core.util.HessianProxyFactoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;


public class SsoWebLoginHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(SsoWebLoginHelper.class);

    /**
     * client login
     *
     * @param response
     * @param sessionId
     * @param ifRemember true: cookie not expire, false: expire when browser close （server cookie）
     * @param xxlUser
     */
    public static void login(HttpServletResponse response, String sessionId, XxlSsoUser xxlUser, boolean ifRemember) {

        String storeKey = null;
        ISsoRpcService ssoRpcService = null;
        try {
            ssoRpcService = HessianProxyFactoryUtil.getHessianBean(ISsoRpcService.class, CacheManager.get());
        } catch (MalformedURLException e) {
            LOGGER.error("connect sso server failed, execption message:{}", e);
        }
        storeKey = ssoRpcService.parseStoreKey(sessionId);
        if (storeKey == null) {
            throw new RuntimeException("parseStoreKey Fail, sessionId:" + sessionId);
        }

        ssoRpcService.put(storeKey, xxlUser);
        CookieUtil.set(response, Conf.SSO_SESSIONID, sessionId, ifRemember);
    }

    /**
     * client logout
     *
     * @param request
     * @param response
     */
    public static void logout(HttpServletRequest request,
                              HttpServletResponse response) {

        String cookieSessionId = CookieUtil.getValue(request, Conf.SSO_SESSIONID);
        if (cookieSessionId == null) {
            return;
        }

        String storeKey = null;
        ISsoRpcService ssoRpcService = null;
        try {
            ssoRpcService = HessianProxyFactoryUtil.getHessianBean(ISsoRpcService.class, CacheManager.get());
        } catch (MalformedURLException e) {
            LOGGER.error("connect sso server failed, execption message:{}", e);
        }

        storeKey = ssoRpcService.parseStoreKey(cookieSessionId);
        if (storeKey != null) {
            ssoRpcService.remove(storeKey);
        }

        CookieUtil.remove(request, response, Conf.SSO_SESSIONID);
    }


    /**
     * login check
     *
     * @param request
     * @param response
     * @return
     */
    public static XxlSsoUser loginCheck(HttpServletRequest request, HttpServletResponse response) {

        String cookieSessionId = CookieUtil.getValue(request, Conf.SSO_SESSIONID);

        // cookie user
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(cookieSessionId);
        if (xxlUser != null) {
            return xxlUser;
        }

        // redirect user

        // remove old cookie
        SsoWebLoginHelper.removeSessionIdByCookie(request, response);

        // set new cookie
        String paramSessionId = request.getParameter(Conf.SSO_SESSIONID);
        xxlUser = SsoTokenLoginHelper.loginCheck(paramSessionId);
        if (xxlUser != null) {
            CookieUtil.set(response, Conf.SSO_SESSIONID, paramSessionId, false);    // expire when browser close （client cookie）
            return xxlUser;
        }

        return null;
    }


    /**
     * client logout, cookie only
     *
     * @param request
     * @param response
     */
    public static void removeSessionIdByCookie(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.remove(request, response, Conf.SSO_SESSIONID);
    }

    /**
     * get sessionid by cookie
     *
     * @param request
     * @return
     */
    public static String getSessionIdByCookie(HttpServletRequest request) {
        String cookieSessionId = CookieUtil.getValue(request, Conf.SSO_SESSIONID);
        return cookieSessionId;
    }
}

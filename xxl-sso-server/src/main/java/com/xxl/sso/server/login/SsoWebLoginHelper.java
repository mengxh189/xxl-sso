package com.xxl.sso.server.login;

import com.xxl.sso.core.conf.Conf;
import com.xxl.sso.core.rpc.ISsoRpcService;
import com.xxl.sso.core.user.XxlSsoUser;
import com.xxl.sso.core.util.CookieUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class SsoWebLoginHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(SsoWebLoginHelper.class);

    @Autowired
    private ISsoRpcService ssoRpcService;
    @Resource
    private SsoTokenLoginHelper ssoTokenLoginHelper;

    /**
     * client login
     *
     * @param response
     * @param sessionId
     * @param ifRemember true: cookie not expire, false: expire when browser close （server cookie）
     * @param xxlUser
     */
    public void login(HttpServletResponse response, String sessionId, XxlSsoUser xxlUser, boolean ifRemember) {

        String storeKey = null;
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
    public void logout(HttpServletRequest request, HttpServletResponse response) {

        String cookieSessionId = CookieUtil.getValue(request, Conf.SSO_SESSIONID);
        if (cookieSessionId == null) {
            return;
        }

        String storeKey = null;
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
    public XxlSsoUser loginCheck(HttpServletRequest request, HttpServletResponse response) {

        String cookieSessionId = CookieUtil.getValue(request, Conf.SSO_SESSIONID);

        // cookie user
        XxlSsoUser xxlUser = ssoTokenLoginHelper.loginCheck(cookieSessionId);
        if (xxlUser != null) {
            return xxlUser;
        }

        // redirect user

        // remove old cookie
        this.removeSessionIdByCookie(request, response);

        // set new cookie
        String paramSessionId = request.getParameter(Conf.SSO_SESSIONID);
        xxlUser = ssoTokenLoginHelper.loginCheck(paramSessionId);
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
    public void removeSessionIdByCookie(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.remove(request, response, Conf.SSO_SESSIONID);
    }

    /**
     * get sessionid by cookie
     *
     * @param request
     * @return
     */
    public String getSessionIdByCookie(HttpServletRequest request) {
        String cookieSessionId = CookieUtil.getValue(request, Conf.SSO_SESSIONID);
        return cookieSessionId;
    }
}

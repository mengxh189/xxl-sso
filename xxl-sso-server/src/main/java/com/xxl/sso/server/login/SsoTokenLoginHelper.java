package com.xxl.sso.server.login;

import com.xxl.sso.core.conf.Conf;
import com.xxl.sso.core.rpc.ISsoRpcService;
import com.xxl.sso.core.user.XxlSsoUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class SsoTokenLoginHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(SsoTokenLoginHelper.class);

    @Autowired
    private ISsoRpcService ssoRpcService;

    /**
     * client login
     *
     * @param sessionId
     * @param xxlUser
     */
    public void login(String sessionId, XxlSsoUser xxlUser) {

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
    public void logout(String sessionId) {

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
    public void logout(HttpServletRequest request) {
        String headerSessionId = request.getHeader(Conf.SSO_SESSIONID);
        logout(headerSessionId);
    }


    /**
     * login check
     *
     * @param sessionId
     * @return
     */
    public XxlSsoUser loginCheck(String sessionId) {

        String storeKey = null;

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
    public XxlSsoUser loginCheck(HttpServletRequest request) {
        String headerSessionId = request.getHeader(Conf.SSO_SESSIONID);
        return loginCheck(headerSessionId);
    }
}

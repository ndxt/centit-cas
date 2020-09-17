package com.centit.framework.cas.actions;

import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.image.CaptchaImageUtil;
import org.apereo.cas.web.support.WebUtils;
import org.springframework.webflow.execution.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public abstract class ActionUtils {

    public static void incLoginTimes(final RequestContext context, int maxTryTimes) {
        HttpServletRequest request = WebUtils.getHttpServletRequestFromExternalWebflowContext(context);
        HttpSession httpSession = request.getSession();
        //request.setAttribute("_loginErrorMessage",errorMsg);
        int failTimes = NumberBaseOpt.castObjectToInteger(
                httpSession.getAttribute("_failValidateTimes"), 0);
        failTimes++;
        if (failTimes >= maxTryTimes) {
            httpSession.setAttribute("_needValidateCode", true);
            httpSession.setAttribute(CaptchaImageUtil.SESSIONCHECKCODE, "session_checkcode_need_change");
        } else {
            httpSession.setAttribute("_needValidateCode", false);
        }
        httpSession.setAttribute("_failValidateTimes", failTimes);
    }
}

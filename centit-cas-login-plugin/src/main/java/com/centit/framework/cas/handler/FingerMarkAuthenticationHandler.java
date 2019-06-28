package com.centit.framework.cas.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.cas.model.FingerMarkCredential;
import com.centit.framework.cas.utils.PrincipalUtils;
import org.apereo.cas.authentication.AuthenticationHandlerExecutionResult;
import org.apereo.cas.authentication.Credential;
import org.apereo.cas.authentication.PreventedException;
import org.apereo.cas.authentication.handler.support.AbstractPreAndPostProcessingAuthenticationHandler;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.services.ServicesManager;

import java.security.GeneralSecurityException;


/**
 * 南大先腾 技术管理中心
 * @author codefan@sina.comc
 * @since 1.0.2
 */
public class FingerMarkAuthenticationHandler extends AbstractPreAndPostProcessingAuthenticationHandler {


    public FingerMarkAuthenticationHandler(String name, ServicesManager servicesManager, PrincipalFactory principalFactory, Integer order) {
        super(name, servicesManager, principalFactory, order);
    }

    @Override
    protected AuthenticationHandlerExecutionResult doAuthentication(Credential credential) throws GeneralSecurityException, PreventedException {
        FingerMarkCredential fingerMarkCredential = (FingerMarkCredential) credential;
        return createHandlerResult(credential,
            this.principalFactory.createPrincipal(fingerMarkCredential.getId(),
                    PrincipalUtils.makePrinciupalAttributes(
                            (JSONObject) JSON.toJSON(fingerMarkCredential))));
    }


    @Override
    public boolean supports(Credential credential) {
        return credential instanceof FingerMarkCredential;
    }

}

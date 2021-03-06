package com.centit.framework.cas.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.cas.model.UsbKeyCredential;
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
public class UsbKeyAuthenticationHandler extends AbstractPreAndPostProcessingAuthenticationHandler {


    public UsbKeyAuthenticationHandler(String name, ServicesManager servicesManager, PrincipalFactory principalFactory, Integer order) {
        super(name, servicesManager, principalFactory, order);
    }

    @Override
    protected AuthenticationHandlerExecutionResult doAuthentication(Credential credential) throws GeneralSecurityException, PreventedException {
        UsbKeyCredential usbKeyCredential = (UsbKeyCredential) credential;
        return createHandlerResult(credential,
            this.principalFactory.createPrincipal( usbKeyCredential.getId(),
                (JSONObject) JSON.toJSON(usbKeyCredential)));
    }


    @Override
    public boolean supports(Credential credential) {
        return credential instanceof UsbKeyCredential;
    }


}

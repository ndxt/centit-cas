package com.centit.framework.cas.handler;

import com.alibaba.fastjson.JSONObject;
import com.centit.framework.cas.config.QueryUserProperties;
import com.centit.framework.cas.model.Md5PasswordCredential;
import com.centit.support.algorithm.BooleanBaseOpt;
import com.centit.support.database.utils.DatabaseAccess;
import com.centit.support.database.utils.DbcpConnectPools;
import org.apache.commons.lang3.StringUtils;
import org.apereo.cas.authentication.AuthenticationHandlerExecutionResult;
import org.apereo.cas.authentication.Credential;
import org.apereo.cas.authentication.PreventedException;
import org.apereo.cas.authentication.UsernamePasswordCredential;
import org.apereo.cas.authentication.exceptions.AccountDisabledException;
import org.apereo.cas.authentication.exceptions.AccountPasswordMustChangeException;
import org.apereo.cas.authentication.handler.support.AbstractPreAndPostProcessingAuthenticationHandler;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.services.ServicesManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.FailedLoginException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.SQLException;


/**
 * 南大先腾 技术管理中心
 * @author codefan@sina.comc
 * @since 1.0.2
 */
public class Md5PasswordAuthenticationHandler extends AbstractPreAndPostProcessingAuthenticationHandler {

    public QueryUserProperties queryUserProperties;

    private PasswordEncoder passwordEncoder;

    public Md5PasswordAuthenticationHandler(String name, ServicesManager servicesManager,
                                            PrincipalFactory principalFactory, Integer order) {
        super(name, servicesManager, principalFactory, order);
    }

    @Override
    protected AuthenticationHandlerExecutionResult doAuthentication(Credential credential) throws GeneralSecurityException, PreventedException {
        //当用户名为admin,并且system为sso即允许通过
        String userName =null;
        String userPwd=null;
        if(credential instanceof Md5PasswordCredential) {
            Md5PasswordCredential pc = (Md5PasswordCredential) credential;
            userName = pc.getUsername();
            userPwd = pc.getPassword();
        }
        if(credential instanceof UsernamePasswordCredential) {
            UsernamePasswordCredential nc = (UsernamePasswordCredential) credential;
            userName = nc.getUsername();
            userPwd = nc.getPassword();
        }
        if (StringUtils.isBlank(userName)) {
            throw new AccountNotFoundException("输入的用户名为空！");
        }
        //这里可以自定义属性数据
        try(Connection conn
                = DbcpConnectPools.getDbcpConnect(queryUserProperties.getDatasource())){
            Integer intParamsTimes = queryUserProperties.getParamRepeatTimes();
            if(intParamsTimes ==null || intParamsTimes<1)
                intParamsTimes = 1;
            Object [] param = new Object[intParamsTimes];
            for(int i=0;i<intParamsTimes;i++){
                param[i] = userName;
            }
            JSONObject user = DatabaseAccess.getObjectAsJSON(conn,queryUserProperties.getSql(), param );
            if(user==null){
                throw new AccountNotFoundException("用户找不到！");
            }
            String password = user.getString( DatabaseAccess.mapColumnNameToField(queryUserProperties.getPinField() ) );
            if(!passwordEncoder.matches(userPwd, password )) {
                throw new FailedLoginException("用户名密码不匹配。");
            }

            if (StringUtils.isNotBlank(queryUserProperties.getDisabledField())) {
                final Object dbDisabled = user.get(DatabaseAccess.mapColumnNameToField(queryUserProperties.getDisabledField()));
                if (BooleanBaseOpt.castObjectToBoolean(dbDisabled,false)) {
                    throw new AccountDisabledException("用户已经失效");
                }
            }

            if (StringUtils.isNotBlank(queryUserProperties.getExpiredField())) {
                final Object dbDisabled = user.get(DatabaseAccess.mapColumnNameToField(queryUserProperties.getExpiredField()));
                if (BooleanBaseOpt.castObjectToBoolean(dbDisabled,false)) {
                    throw new AccountPasswordMustChangeException("密码已过期");
                }
            }

            String principal = userName;
            String principalKey = queryUserProperties.getPrincipalField();
            if(StringUtils.isNotBlank(principalKey) && !"none".equalsIgnoreCase(principalKey)){
                String tempPrincipal = user.getString(DatabaseAccess.mapColumnNameToField(principalKey));
                if(StringUtils.isNotBlank(tempPrincipal)){
                    principal = tempPrincipal;
                }
            }
            user.remove(DatabaseAccess.mapColumnNameToField(queryUserProperties.getPinField() ));
            return createHandlerResult(credential, this.principalFactory.createPrincipal( principal, user));

        }catch (SQLException | IOException e) {
            throw new AccountNotFoundException("查找用户 "+userName+" 报错 "+ e.getLocalizedMessage());
        }
    }


    @Override
    public boolean supports(Credential credential) {
        return (credential instanceof Md5PasswordCredential)
            || (credential instanceof UsernamePasswordCredential);
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public void setQueryUserProperties(QueryUserProperties queryUserProperties) {
        this.queryUserProperties = queryUserProperties;
    }
}

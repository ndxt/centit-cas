package com.centit.framework.cas.config;

import com.centit.framework.cas.handler.LdapAuthenticationHandler;
import com.centit.framework.cas.handler.Md5PasswordAuthenticationHandler;
import com.centit.framework.cas.utils.StandardPasswordEncoder;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlan;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlanConfigurer;
import org.apereo.cas.authentication.AuthenticationHandler;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.authentication.principal.PrincipalResolver;
import org.apereo.cas.services.ServicesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 南大先腾 技术管理中心
 * @author codefan@sina.comc
 * @since 1.0.2
 */
@Configuration("complexAuthEventExecutionPlanConfiguration")
@EnableConfigurationProperties(ComplexConfigurationProperties.class)
public class ComplexAuthEventExecutionPlanConfiguration
        implements AuthenticationEventExecutionPlanConfigurer
        /*,CasWebflowExecutionPlanConfigurer */{

    @Autowired
    private ComplexConfigurationProperties complexProperties;

    @Autowired
    @Qualifier("servicesManager")
    private ServicesManager servicesManager;

    @Autowired
    @Qualifier("acceptUsersPrincipalFactory")
    private PrincipalFactory acceptUsersPrincipalFactory;

    @Autowired
    @Qualifier("personDirectoryPrincipalResolver")
    private PrincipalResolver personDirectoryPrincipalResolver;
    /**
     * 注册验证器
     * @return
     */
    @Bean
    public AuthenticationHandler md5PasswordAuthenticationHandler() {
        //优先验证
        Md5PasswordAuthenticationHandler authenticationHandler =
            new Md5PasswordAuthenticationHandler("md5PasswordAuthenticationHandler",
                servicesManager, acceptUsersPrincipalFactory, 1);
        authenticationHandler.setPasswordEncoder(new StandardPasswordEncoder());
        authenticationHandler.setQueryUserProperties(complexProperties.getQueryUser());
        return authenticationHandler;
    }

    @Bean
    public AuthenticationHandler ldapAuthenticationHandler() {
        //优先验证
        LdapAuthenticationHandler authenticationHandler =
                new LdapAuthenticationHandler("ldapAuthenticationHandler",
                        servicesManager, acceptUsersPrincipalFactory, 1);
        authenticationHandler.setLdapProperties(complexProperties.getLdap());
        authenticationHandler.setSyncUserProperties(complexProperties.getSyncUser());
        return authenticationHandler;
    }

    //注册自定义认证器
    @Override
    public void configureAuthenticationExecutionPlan(final AuthenticationEventExecutionPlan plan) {
        //CentitPrincipalResolver resolver = new CentitPrincipalResolver();
        //plan.
        plan.registerAuthenticationHandler(md5PasswordAuthenticationHandler());
        plan.registerAuthenticationHandler(ldapAuthenticationHandler());

        plan.registerAuthenticationHandlerWithPrincipalResolver(md5PasswordAuthenticationHandler(),
                personDirectoryPrincipalResolver);

    }

}

package com.centit.framework.cas.resolver;

import com.centit.framework.cas.model.ComplexAuthCredential;
import org.apache.commons.lang3.StringUtils;
import org.apereo.cas.authentication.AuthenticationHandler;
import org.apereo.cas.authentication.Credential;
import org.apereo.cas.authentication.principal.Principal;
import org.apereo.cas.authentication.principal.PrincipalResolver;
import org.apereo.services.persondir.IPersonAttributeDao;

import java.util.Optional;

public class CentitPrincipalResolver implements PrincipalResolver {

    @Override
    public Principal resolve(final Credential credential,
                             final Optional<Principal> principal,
                             final Optional<AuthenticationHandler> handler) {
        return principal.isPresent() ? principal.get() :
                (credential instanceof ComplexAuthCredential ?
                        CentitPrincipal.createPrincipal((ComplexAuthCredential)credential): null);
    }

    @Override
    public boolean supports(final Credential credential) {
        return StringUtils.isNotBlank(credential.getId());
    }

    @Override
    public IPersonAttributeDao getAttributeRepository() {
        return null;
    }
}

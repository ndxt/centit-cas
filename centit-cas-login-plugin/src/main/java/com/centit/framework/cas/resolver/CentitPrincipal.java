package com.centit.framework.cas.resolver;

import com.centit.framework.cas.model.ComplexAuthCredential;
import org.apereo.cas.authentication.principal.Principal;
public class CentitPrincipal implements Principal {

    private ComplexAuthCredential credential;
    /**
     * @return the unique id for the Principal
     */
    @Override
    public String getId() {
        return credential.getId();
    }

    public static CentitPrincipal createPrincipal(ComplexAuthCredential credential){
        CentitPrincipal principal = new CentitPrincipal();
        principal.credential = credential;
        return principal;
    }
}

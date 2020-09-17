package com.centit.framework.cas.actions;

import com.centit.framework.cas.config.StrategyProperties;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

public class AuthenticationFailureAction extends AbstractAction {
    private StrategyProperties strategyConfig;

    @Override
    protected Event doExecute(RequestContext context) throws Exception {
        ActionUtils.incLoginTimes(context, this.strategyConfig.getMaxFailTimesBeforeValidateCode());
        return new Event(this, "success");
    }

    public void setStrategyConfig(StrategyProperties strategyConfig) {
        this.strategyConfig = strategyConfig;
    }

}

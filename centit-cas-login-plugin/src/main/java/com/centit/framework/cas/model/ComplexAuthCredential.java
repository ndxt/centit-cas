package com.centit.framework.cas.model;

import org.apereo.cas.authentication.Credential;

import java.io.Serializable;

public abstract class ComplexAuthCredential implements Credential, Serializable {

    private String authType;
    private String macAddr;

    public ComplexAuthCredential(String authType) {
        this.authType = authType;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    /**
     * 检查输入是否符合要求
     * @return 返回验证结果，null 为没有错误
     */
    public String checkInput(){
        return null;
    }
}

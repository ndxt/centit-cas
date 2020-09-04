package com.centit.framework.cas.config;

import com.centit.support.database.utils.DataSourceDescription;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.io.Serializable;

public class SyncUserProperties implements Serializable {

    private static final long serialVersionUID = 1L;
    private String enable;
    private String connUrl;
    private String username;
    private String password;
    private String queryUserSql;
    private String insertUserSql;
    private String queryUnitSql;
    private String insertUnitSql;
    private String queryUserUnitSql;
    private String insertUserUnitSql;

    @NestedConfigurationProperty
    private DataSourceDescription datasource = new DataSourceDescription();

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public String getConnUrl() {
        return connUrl;
    }

    public void setConnUrl(String connUrl) {
        this.connUrl = connUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getQueryUserSql() {
        return queryUserSql;
    }

    public void setQueryUserSql(String queryUserSql) {
        this.queryUserSql = queryUserSql;
    }

    public String getInsertUserSql() {
        return insertUserSql;
    }

    public void setInsertUserSql(String insertUserSql) {
        this.insertUserSql = insertUserSql;
    }

    public String getQueryUnitSql() {
        return queryUnitSql;
    }

    public void setQueryUnitSql(String queryUnitSql) {
        this.queryUnitSql = queryUnitSql;
    }

    public String getInsertUnitSql() {
        return insertUnitSql;
    }

    public void setInsertUnitSql(String insertUnitSql) {
        this.insertUnitSql = insertUnitSql;
    }

    public String getQueryUserUnitSql() {
        return queryUserUnitSql;
    }

    public void setQueryUserUnitSql(String queryUserUnitSql) {
        this.queryUserUnitSql = queryUserUnitSql;
    }

    public String getInsertUserUnitSql() {
        return insertUserUnitSql;
    }

    public void setInsertUserUnitSql(String insertUserUnitSql) {
        this.insertUserUnitSql = insertUserUnitSql;
    }

    public DataSourceDescription getDatasource() {
        return datasource;
    }

    public void setDatasource(DataSourceDescription datasource) {
        this.datasource = datasource;
    }

}

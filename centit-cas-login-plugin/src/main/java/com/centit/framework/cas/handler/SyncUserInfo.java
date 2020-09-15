package com.centit.framework.cas.handler;

import com.alibaba.fastjson.JSONObject;
import com.centit.framework.cas.config.SyncUserProperties;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.UuidOpt;
import com.centit.support.database.utils.DatabaseAccess;
import com.centit.support.database.utils.DbcpConnectPools;
import com.centit.support.database.utils.TransactionHandler;
import org.apereo.cas.authentication.principal.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 从配置文件读入配置信息 和 同步参数
 * complex.syncUser.
 */
public class SyncUserInfo {

    private static Logger logger = LoggerFactory.getLogger(SyncUserInfo.class);

    private SyncUserProperties syncUserProperties;

    public void setSyncUserProperties(SyncUserProperties syncUserProperties) {
        this.syncUserProperties = syncUserProperties;
    }

    private void syncUserInfo(Connection conn, Principal principal) throws IOException, SQLException {
        //判断同步人员、机构信息
        Object[] unitParam = new Object[1];
        unitParam[0] = principal.getAttributes().get("memberOf");
        JSONObject unit = DatabaseAccess.getObjectAsJSON(conn, syncUserProperties.getQueryUnitSql(), unitParam);
        String unitcode = UuidOpt.getUuidAsString22();
        if (null == unit) {
            Map<String, Object> params = makeUnitParams(principal.getAttributes());
            params.put("unitcode", unitcode);
            params.put("unitpath", "/" + unitcode);
            DatabaseAccess.doExecuteNamedSql(conn, syncUserProperties.getInsertUnitSql(), params);
        } else {
            unitcode = unit.getString("unitCode");
        }

        Object[] userParam = new Object[1];
        userParam[0] = principal.getAttributes().get("sAMAccountName");
        JSONObject user = DatabaseAccess.getObjectAsJSON(conn, syncUserProperties.getQueryUserSql(), userParam);
        String usercode = UuidOpt.getUuidAsString22();
        if (null == user) {
            Map<String, Object> params = makeUserParams(principal.getAttributes());
            params.put("usercode", usercode);
            params.put("primaryunit", unitcode);
            DatabaseAccess.doExecuteNamedSql(conn, syncUserProperties.getInsertUserSql(), params);
        } else {
            usercode = user.getString("userCode");
        }

        Object[] param = new Object[2];
        param[0] = usercode;
        param[1] = unitcode;
        Long userUnit = DatabaseAccess.queryTotalRows(conn, syncUserProperties.getQueryUserUnitSql(), param);
        if (userUnit == 0) {
            Map<String, Object> params = makeUserUnitParams(principal.getAttributes());
            params.put("unitcode", unitcode);
            params.put("usercode", usercode);
            DatabaseAccess.doExecuteNamedSql(conn, syncUserProperties.getInsertUserUnitSql(), params);
        }
    }

    private static Map<String, Object> makeUnitParams(Map<String, Object> principal) {
        Map<String, Object> params = new HashMap<>(20);
        //String unitcode = UuidOpt.getUuidAsString22();
        //params.put("unitcode",unitcode);
        params.put("unittag", principal.get("memberOf"));
        params.put("isvalid", "T");
        params.put("unittype", "A");
        //params.put("unitpath", "/" + unitcode);
        params.put("createdate", DatetimeOpt.currentUtilDate());
        params.put("unitname", principal.get("description"));
        //params.put("unitdesc", principal.get("distinguishedName"));
        params.put("unitdesc", "");
        params.put("updatedate", DatetimeOpt.currentUtilDate());
        return params;
    }

    private static Map<String, Object> makeUserParams(Map<String, Object> principal) {
        Map<String, Object> params = new HashMap<>(20);
        //params.put("usercode", UuidOpt.getUuidAsString22());
        //params.put("userpin", principal.get(""));
        params.put("userpin", "");
        params.put("usertype", "U");
        params.put("isvalid", "T");
        params.put("loginname", principal.get("sAMAccountName"));
        params.put("username", principal.get("displayName"));
        params.put("usertag", principal.get("distinguishedName"));
        params.put("regemail", principal.get("mail"));
        //params.put("primaryunit", principal.get(""));
        params.put("userword", principal.get("jobNo"));
        params.put("userorder", 1000);
        params.put("updatedate", DatetimeOpt.currentUtilDate());
        params.put("createdate", DatetimeOpt.currentUtilDate());
        return params;
    }

    private static Map<String, Object> makeUserUnitParams(Map<String, Object> principal) {
        Map<String, Object> params = new HashMap<>(20);
        params.put("userunitid", UuidOpt.getUuidAsString22());
        params.put("isprimary", "F");
        params.put("userstation", "ZY");
        params.put("userrank", "YG");
        params.put("userorder", 1000);
        params.put("updatedate", DatetimeOpt.currentUtilDate());
        params.put("createdate", DatetimeOpt.currentUtilDate());
        return params;
    }

    public void syncUser(Principal principal){
        try {
            TransactionHandler.executeInTransaction(
                    syncUserProperties.getDatasource(),
                    (conn) ->{
                        try{
                            syncUserInfo(conn, principal);
                            return 1;
                        } catch (IOException e){
                            logger.error(e.getLocalizedMessage());
                            return -1;
                        }
                    });
        } catch (SQLException  e) {
            logger.error(e.getLocalizedMessage());
        }
    }

}

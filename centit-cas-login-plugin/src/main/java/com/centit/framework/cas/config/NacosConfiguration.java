package com.centit.framework.cas.config;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.annotation.NacosProperties;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.spring.context.annotation.EnableNacos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author zfg
 */
@Component
@EnableNacos(
        globalProperties =
        @NacosProperties(serverAddr = "192.168.137.49:8847,192.168.137.49:8848,192.168.137.49:8849")
)
public class NacosConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(NacosConfiguration.class);

    @Value("${nacos.serviceName}")
    private String serviceName;

    @Value("${nacos.group}")
    private String group;

    @Value("${server.port}")
    private int port;

    @NacosInjected
    private NamingService namingService;

    @NacosInjected(properties = @NacosProperties(encode = "UTF-8"))
    private NamingService namingServiceUTF8;

    @NacosInjected
    private ConfigService configService;

    @PostConstruct
    public void init() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            if (namingService != namingServiceUTF8) {
                throw new RuntimeException("Why?");
            } else {
                namingService.registerInstance(serviceName, group, address.getHostAddress(), port);
            }
        } catch (UnknownHostException | NacosException e) {
            e.printStackTrace();
            logger.error("nacos exception:", e);
        }
    }

}

package com.polaris.lesscode.permission;

import io.sentry.Sentry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * role run class
 *
 * @author roamer
 * @version v1.0
 * @date 2020-08-31 10:30
 */
@EnableConfigurationProperties
@EnableFeignClients(basePackages = {
        "com.polaris.lesscode.form.internal.feign",
		"com.polaris.lesscode.uc.internal.feign",
        "com.polaris.lesscode.dc.internal.feign",
        "com.polaris.lesscode.app.internal.feign",
        "com.polaris.lesscode.gotable.internal.feign",
        "com.polaris.lesscode.gopush.internal.feign"})
@EnableAspectJAutoProxy(proxyTargetClass = true)
@SpringBootApplication
public class PermissionApplication {

    public static void main(String[] args) {
        String env = System.getenv("SERVER_ENVIROMENT");
        Sentry.getStoredClient().setEnvironment(env);
        SpringApplication.run(PermissionApplication.class, args);
    }
}

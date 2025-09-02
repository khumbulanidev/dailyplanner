package com.khumbu.dailyplanner.config;

import com.khumbu.dailyplanner.utils.EncryptionUtil;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "spring.datasource")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataSourceInfo {
    private static String url;
    private static String username;
    private String password;
    private static String driver;

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        DataSourceInfo.url = url;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        DataSourceInfo.username = username;
    }

    public String getPassword() {
        return  this.password;
    }

    public void setPassword(String password) {
        this.password = EncryptionUtil.decrypt(password);
    }

    public static String getDriver() {
        return driver;
    }

    public static void setDriver(String driver) {
        DataSourceInfo.driver = driver;
    }
}

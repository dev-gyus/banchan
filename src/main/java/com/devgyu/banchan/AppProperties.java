package com.devgyu.banchan;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("app")
@Data
public class AppProperties {
    private String host;
    private String uploadFolderPrefix;
    private String nowProfile;
}

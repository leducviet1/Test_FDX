package com.example.librarymanage_be.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.minio")
@Data
public class MinioProperties {
    private String url;
    private String accessKey;
    private String secretKey;
    private Bucket bucket;
    @Data
    public static class Bucket{
        private String avatar;
        private String report;
    }
}

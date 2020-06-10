package com.kube.config;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.util.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;


@Configuration
@ConfigurationProperties("kubernetes.config")
public class KubeConfig {
    private String configPath;

    public String getConfigPath() {
        return configPath;
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    @Bean(name = "kubeApiClient")
    public ApiClient apiClient() {
        ApiClient kubeApiClient = null;
        try {
            kubeApiClient = Config.fromConfig(configPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        kubeApiClient.setConnectTimeout(1800000);
        kubeApiClient.setReadTimeout(1800000);
        kubeApiClient.setWriteTimeout(1800000);

        io.kubernetes.client.openapi.Configuration.setDefaultApiClient(kubeApiClient);
        return kubeApiClient;
    }

}

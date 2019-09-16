package com.kube.config;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.util.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


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
        kubeApiClient.getHttpClient().setConnectTimeout(3600, TimeUnit.SECONDS);
        kubeApiClient.getHttpClient().setReadTimeout(3600, TimeUnit.SECONDS);
        kubeApiClient.getHttpClient().setWriteTimeout(3600, TimeUnit.SECONDS);

        io.kubernetes.client.Configuration.setDefaultApiClient(kubeApiClient);
        return kubeApiClient;
    }

}

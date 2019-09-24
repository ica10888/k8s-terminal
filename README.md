# k8s-web-terminal

一个k8s web终端连接工具,java轻量实现。

### Detail

![avatar](https://github.com/ica10888/k8s-web-terminal/blob/master/doc/example.png?raw=true)

Spring boot 集成，模拟ssh连接k8s集群。

### Config


src/main/resources/application.yml

``` yaml
kubernetes:
  config:
    config-path: /root/.kube/config
```

配置 kubernetes config 文件即可连接集群。

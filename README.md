# k8s-web-terminal

a k8s web terminal connect tool, java lightweight implementation.

### Detail

![avatar](https://github.com/ica10888/k8s-web-terminal/blob/master/doc/example.png?raw=true)

Spring boot integrated，which mimic ssh to connect k8s cluster. 

### Config


src/main/resources/application.yml

``` yaml
kubernetes:
  config:
    config-path: /root/.kube/config
```

 Set up kubernetes config yaml ,connect k8s cluster.

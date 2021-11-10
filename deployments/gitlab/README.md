# Gitlab Install Guide

---
## pre required

local k8s에서 실행시 `hosts` 정보 수정 *domain이 있다면 생략
```shell
$ vi /etc/hosts
```

`/etc/hosts` 수정
```shell
127.0.0.1 gitlab.greenmarket.com
127.0.0.1 registry.greenmarket.com
127.0.0.1 minio.greenmarket.com 
```


---
## gitlab helm install
```shell
$ helm repo add gitlab https://charts.gitlab.io
$ kubectl create ns cicd
$ helm install -n cicd -f override-gitlab-values.yaml gitlab gitlab/gitlab
```
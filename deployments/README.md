# k8s infra setting Guide


## helm install
```shell
// helm install
$ curl -fsSL -o get_helm.sh https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-3
$ chmod 700 get_helm.sh
$ ./get_helm.sh

// helm repository add
$ helm repo add stable https://charts.helm.sh/stable
$ helm repo update
$ helm repo list
```


## nginx ingress install
```shell
helm upgrade --install ingress-nginx ingress-nginx \
  --repo https://kubernetes.github.io/ingress-nginx \
  --namespace ingress-nginx --create-namespace
```
# brightlizard-otus-k8s
https://github.com/schetinnikov-otus/arch-labs

sudo docker build -t brightlizard/brightlizard-otus-k8s-app:1.4.0 .
sudo docker run -p 8080:8080 brightlizard/brightlizard-otus-k8s-app:1.4.0

sudo kubectl create ns otus
sudo kubectl config set-context --current --namespace=otus

sudo kubectl scale --replicas=0 deployment.apps/postgres
sudo kubectl delete pvc --all
sudo kubectl delete pv --all


----- HELM -----
sudo helm dependency build otus-app
sudo helm install otus-app-helm ./otus-app/
sudo helm install --dry-run --debug otus-app-helm ./otus-app/
sudo helm delete otus-app-helm
sudo kubectl delete pvc data-otus-app-helm-postgresql-0
sudo kubectl exec -ti pod/otus-app-helm-postgresql-0 bash

helm upgrade --debug otus-app-helm ./otus-app/
helm upgrade --set version=v2 --debug otus-app-helm-v2 ./otus-app/

sudo helm install --debug otus-proxyapp ./otus-app/


helm delete --purge my-postgresql
helm repo remove bitnami
kubectl delete -f persistent-volume-claim-postgresql.yaml
kubectl delete -f persistent-volume-postgresql.yaml
kubectl delete -f namespace.yaml

minikube stop
minikube delete

sudo kubectl get validatingwebhookconfigurations

-----------
sudo rm -rf /tmp/*
sudo minikube start
sudo minikube start --vm-driver=none
sudo minikube addons enable ingress


--- Prometheus ---
sudo helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
sudo helm repo add stable https://charts.helm.sh/stable
sudo helm repo update
sudo helm install prom prometheus-community/kube-prometheus-stack -f prometheus.yaml --atomic
sudo kubectl get all
sudo helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
sudo helm repo update
sudo helm install nginx ingress-nginx/ingress-nginx -f nginx-ingress.yaml --atomic


--- CURL ---
curl $(sudo minikube service otus-app-helm-nodeport -n otus --url)/health
curl $(sudo minikube service otus-app-helm-nodeport -n otus --url)/echo

export INGRESS_PORT=$(sudo kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.spec.ports[?(@.name=="http2")].nodePort}')
export SECURE_INGRESS_PORT=$(sudo kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.spec.ports[?(@.name=="https")].nodePort}')
export INGRESS_HOST=$(sudo minikube ip)
sudo minikube tunnel

export INGRESS_HOST=$(sudo kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.spec.clusterIP}')
export GATEWAY_URL=$INGRESS_HOST:$INGRESS_PORT

export INGRESS_PORT=$(kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.spec.ports[?(@.name=="http2")].nodePort}')
export SECURE_INGRESS_PORT=$(kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.spec.ports[?(@.name=="https")].nodePort}')
export TCP_INGRESS_PORT=$(kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.spec.ports[?(@.name=="tcp")].nodePort}')
export INGRESS_HOST=$(minikube ip)
export GATEWAY_URL=$INGRESS_HOST:$INGRESS_PORT
echo $GATEWAY_URL
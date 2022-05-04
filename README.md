# brightlizard-otus-k8s
https://github.com/schetinnikov-otus/arch-labs

sudo docker build -t brightlizard/brightlizard-otus-k8s-app:1.0.3 .
sudo docker run -p 8080:8080 brightlizard-otus-k8s-app:1.0.3

sudo kubectl create ns otus
sudo kubectl config set-context --current --namespace otus

sudo kubectl scale --replicas=0 deployment.apps/postgres
sudo kubectl delete pvc --all
sudo kubectl delete pv --all
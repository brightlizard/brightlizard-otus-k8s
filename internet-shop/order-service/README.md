sudo docker build -t brightlizard/brightlizard-otus-ishop-order-service:1.0.0 .
sudo docker run -p 4321:4321 brightlizard/brightlizard-ishop-order-service:1.0.0

-------- K8s ---------
sudo kubectl config set-context --current --namespace=monitoring
sudo kubectl config set-context --current --namespace=otus

-------- HELM --------
sudo helm install --debug ishop-order-service ./ishop-order-service/
sudo helm upgrade --debug ishop-order-service ./ishop-order-service/
sudo helm delete --debug ishop-order-service

-------- Prometheus --------
sudo kubectl port-forward service/prom-grafana 9000:80
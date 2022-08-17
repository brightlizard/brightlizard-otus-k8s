sudo docker build -t brightlizard/brightlizard-otus-internet-shop-app:1.0.1 .
sudo docker run -p 4321:4321 brightlizard/brightlizard-otus-internet-shop-app:1.0.1

-------- HELM --------
sudo helm install --debug otus-internet-shop-app ./otus-internet-shop-app/
sudo helm upgrade --debug otus-internet-shop-app ./otus-internet-shop-app/
sudo helm delete --debug otus-internet-shop-app


NOTES:
1. Get the application URL by running these commands:
  export POD_NAME=$(kubectl get pods --namespace otus -l "app.kubernetes.io/name=otus-internet-shop-app,app.kubernetes.io/instance=otus-internet-shop-app" -o jsonpath="{.items[0].metadata.name}")
  export CONTAINER_PORT=$(kubectl get pod --namespace otus $POD_NAME -o jsonpath="{.spec.containers[0].ports[0].containerPort}")
  echo "Visit http://127.0.0.1:8080 to use your application"
  kubectl --namespace otus port-forward $POD_NAME 8080:$CONTAINER_PORT

# Install Nexus on Kubernetes

## 필요사항 

- Kubernetes cluster (minikube, minishift, microk8s 등) 준비
- nexus를 위한 지정된 namespace가 필요하다. (우리는 여기서 nexus로 이용할 것이다.)

## Step 

### 1. Create a namespace for nuxus 

- nexus를 설치하기 위해서 namespace를 설치할 것이다. 

```go
kubectl create namespace nexus
```

### 2. Create the deployment file 

- Deployment.yaml 파일을 생성하자. 
- 이는 nexus 배포를 위해 기술한다.
  
- nexus 2.x 와 nexus 3.x 사이에는 약간의 차이가 있다. 
- 두 케이스에서 우리는 nexus-data에 대한 볼륨 마운트를 이용한다. 
- 기억할 것은 pod가 제거되면 이들도 같이 제거된다는 것이다. 
- 프러덕션 모드에서는 data 저장을 위해서 몇가지 다른 종류의 방법을 사용해야한다. 

#### Nexus 2.x

- 여기에는 몇가지 사용자 지정 가능한 ENV 변수를 전달하고, 넥서스 데이터에 대한 볼륨 마운트를 추가한다. 
- Deployment.yaml

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: nexus
  namespace: nexus
spec:
  replicas: 1
  template:
    metadata:
      labels:
        app: nexus-server
    spec:
      containers:
      - name: nexus
        image: sonatype/nexus:latest
        env:
        - name: MAX_HEAP
          value: "800m"
        - name: MIN_HEAP
          value: "300m"
        resources:
          limits:
            memory: "4Gi"
            cpu: "1000m"
          resources:
            memory: "2Gi"
            cpu: "500m"
        ports:
        - containerPort: 8081
        volumeMounts:
        - name: nexus-data
          mountPath: /sonatype-work
      volumes:
      - name: nexus-data
        emptyDir: {}
```

#### Nexus 3.x

- Nexus 3.x 에 대해서 우리는 다른 커스텀 환경 변수를 사용하지 않는다.
- 환경 변수를 위해서는 docker 리포지토리를 확인하자. 
- Deployment.yaml

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: nexus
  namespace: nexus
spec:
  replicas: 1
  selector:
    matchLabels:
      app: nexus-server
  template:
    metadata:
      labels:
        app: nexus-server
    spec:
      containers:
        - name: nexus
          image: sonatype/nexus3:latest
          resources:
            limits:
              memory: "500Mi"
              cpu: "1000m"
            requests:
              memory: "100Mi"
              cpu: "500m"
          ports:
            - containerPort: 8081
          volumeMounts:
            - name: nexus-data
              mountPath: /nexus-data
      volumes:
        - name: nexus-data
          emptyDir: {}
```

### 3. Create the deployment

- 이제 deployment 를 시작하자. 

```go
kubectl create -f Deployment.yaml
```

```go
kubectl get po -n nexus
```

### 4. Create Service 

- service.yaml 파일ㅇ르 생성하고, nexus의 엔드포인트를 NodePort를 이용하여 노출시켜 보자. 
- 노트: 
  - 만약 클라우드에서 이용한다면, 서비스를 loadbalancer를 이용하여 노출하며 이때 서비스 타입은 LoadBalancer이다. 
  - 또한 Prometheus 어노테이션을 Prometheus에 의해서 모니터링 엔드포인트에 도움을 준다. 

```yaml
apiVersion: v1
kind: Service
metadata:
  name: nexus-service
  namespace: nexus
  annotations:
    prometheus.io/scrape: 'true'
    prometheus.io/path: /
    prometheus.io/port: '8081'
spec:
  selector:
    app: nexus-server
  type: NodePort
  ports:
  - port: 8081
    targetPort: 8081
    nodePort: 32000
```

```
kubectl create -f service.yaml

kubectl describe service nexus-service
```

### port-forward

- 만약 port-forward 를 이용해 보고 싶다면 (테스트용으로 사용) 다음과 같이 커맨드를 수행해보자. 

```
kubectl port-forward deployment/nexus 32000:8081 -n nexus
```

#### Nexus 2.x


```
http://localhost:32000/nexus
```

#### Nexus 3.x

```
http://localhost:32000
```

#### nexus 암호 

- nexus 2.x 는 admin/admin123 이다. 
- nexus 3.x 는 내부에 자동으로 생성된다. 
  - /nexus-data/admin.password 위치에 존재한다. 
  - 다음과 같이 수정하자. 
 
```
kubectl exec nexus-23424234-xcsdf -n nexus -- cat /nexus-data/admin.password
```

## Persistance Volume 연동한 배포 버젼

### 1. Namespace 생성하기. 

[NexusNamespace](01.NexusNamespace.yaml)

### 2. Persistance Volume 생성하기. 

[Persistance Volume](02.PersistanceVolume.yaml)

### 3. Persistance Volume Claim 생성하기. 

[Persistance Volume Claim](03.PersistanceVolumeClaim.yaml)

### 4. Nexus Deployment (3.x버젼)

[Nexus3x Deployment](05.Nexus3xDeployment.yaml)

### 5. Nexus Service 오픈하기 (NodePort 이용버젼)

[Nexus Service](06.NexusService.yaml)
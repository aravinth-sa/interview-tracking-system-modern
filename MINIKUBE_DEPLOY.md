# Minikube Deployment Guide
## Interview Tracking System — Local K8s Learning

---

## Prerequisites

Before starting, make sure these 3 tools are installed on your machine.

### Check if they are installed

```bash
minikube version
kubectl version --client
docker --version
```

### Install if missing

| Tool | Download |
|------|----------|
| Docker Desktop | https://www.docker.com/products/docker-desktop |
| Minikube | https://minikube.sigs.k8s.io/docs/start |
| kubectl | Comes bundled with Docker Desktop |

### Why do you need all 3?

| Tool | Role |
|------|------|
| **Docker** | Builds your Spring Boot app into a container image |
| **Minikube** | Creates a single-node K8s cluster on your laptop |
| **kubectl** | CLI to talk to K8s — apply manifests, check pods, read logs |

---

## Step 1 — Start Minikube

```bash
minikube start
```

**Why?**
Minikube creates a mini Kubernetes cluster inside a VM on your machine.
Think of it as a one-node version of a real cloud cluster.
Nothing else works until this is running.

### Verify it started correctly

```bash
minikube status
```

Expected output:
```
minikube
type: Control Plane
host: Running
kubelet: Running
apiserver: Running
kubeconfig: Configured
```

```bash
kubectl get nodes
```

Expected output:
```
NAME       STATUS   ROLES           AGE   VERSION
minikube   Ready    control-plane   1m    v1.x.x
```

---

## Step 2 — Point Docker to Minikube's Internal Registry

**Mac / Linux:**
```bash
eval $(minikube docker-env)
```

**Windows (PowerShell):**
```powershell
minikube docker-env | Invoke-Expression
```

**Why?**
This is the most confusing step for beginners. Here is what is happening:

- Your laptop has its own Docker daemon
- Minikube has its own separate Docker daemon inside the VM
- If you build an image on your laptop's Docker, Minikube cannot see it

This command re-points your terminal's Docker to Minikube's internal Docker daemon.
So when you build next, the image lands inside Minikube where K8s can pull it.

```
Without this:
  docker build → your laptop's Docker → K8s cannot find image → ImagePullBackOff error

With this:
  docker build → Minikube's Docker → K8s finds it instantly ✓
```

> **Important:** This only applies to the current terminal session.
> If you open a new terminal, run this command again before building.

### Verify Docker is now pointing to Minikube

```bash
docker info | grep Name
```

You should see `minikube` in the output.

---

## Step 3 — Build the JAR

```bash
cd d:/its-revamp/its-spring-boot-modern
mvn clean package -DskipTests
```

**Why?**
- `clean` — deletes the old `target/` folder so you start fresh
- `package` — compiles your code and packages it into a fat JAR
- `-DskipTests` — skips running tests to speed up the build

The output JAR will be at:
```
target/interview-tracking-system-2.0.0.jar
```

---

## Step 4 — Build the Docker Image Inside Minikube

```bash
docker build -t its-app:2.0.0 .
```

**Why?**
- Takes your JAR + the `Dockerfile` and creates a container image
- Image is named `its-app:2.0.0` — this matches what `k8s/app/deployment.yaml` references
- Because of Step 2, this image is built inside Minikube's Docker, not your laptop's

### Verify the image is available

```bash
docker images | grep its-app
```

Expected output:
```
its-app   2.0.0   abc123def   1 minute ago   xxx MB
```

---

## Step 5 — Apply Kubernetes Manifests in Order

K8s resources depend on each other, so the order matters.

### 5a — Create the Namespace

```bash
kubectl apply -f k8s/namespace.yaml
```

**Why first?**
Every other resource uses `namespace: its`.
The namespace must exist before anything else can be created inside it.

### 5b — Create ConfigMap and Secret

```bash
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/secret.yaml
```

**Why before Deployments?**
The app and postgres Deployments reference these via `envFrom`.
If they don't exist when the Deployment is created, the pods will fail to start.

### 5c — Deploy PostgreSQL

```bash
kubectl apply -f k8s/postgres/
```

This applies all 3 files in the postgres folder:
- `pvc.yaml` — claims a 1GB persistent disk for PostgreSQL data
- `deployment.yaml` — runs the PostgreSQL pod
- `service.yaml` — creates the internal DNS name `its-postgres-service`

**Why before the app?**
Spring Boot connects to PostgreSQL on startup.
If the DB is not ready, the app will fail its readiness probe and keep restarting.

### 5d — Wait for PostgreSQL to be Ready

```bash
kubectl wait --for=condition=ready pod -l app=its-postgres -n its --timeout=60s
```

**Why?**
This command pauses your terminal until the PostgreSQL pod passes its readiness probe.
Without this wait, you might deploy the app before the DB is up, causing restarts.

### 5e — Deploy the Spring Boot App

```bash
kubectl apply -f k8s/app/
```

This applies all 3 files in the app folder:
- `deployment.yaml` — runs 2 Spring Boot pods
- `service.yaml` — exposes the app via NodePort
- `hpa.yaml` — auto-scaler (scales 2 to 5 pods when CPU > 70%)

---

## Step 6 — Watch the Pods Start

```bash
kubectl get pods -n its -w
```

The `-w` flag watches in real time. You will see pods move through these states:

```
NAME                          READY   STATUS
its-postgres-xxx              0/1     ContainerCreating   ← pulling image
its-postgres-xxx              1/1     Running             ← healthy ✓
its-app-xxx                   0/1     ContainerCreating   ← pulling image
its-app-xxx                   0/1     Running             ← Spring Boot starting (takes ~30s)
its-app-xxx                   1/1     Running             ← readiness probe passed ✓
its-app-yyy                   1/1     Running             ← second pod up ✓
```

Press `Ctrl+C` to stop watching once both app pods show `1/1 Running`.

---

## Step 7 — Access the App

```bash
minikube service its-app-service -n its
```

**Why not localhost?**
Minikube runs inside a VM — it has its own IP address, not `localhost`.
This command creates a tunnel from your laptop to the NodePort service
and opens the URL in your browser automatically.

You will get a URL like: `http://192.168.49.2:31xxx`

### Test endpoints

| URL | What it is |
|-----|-----------|
| `http://<minikube-ip>/swagger-ui.html` | Swagger UI — test all APIs |
| `http://<minikube-ip>/actuator/health` | Health check |
| `http://<minikube-ip>/h2-console` | Not available in prod profile |

### Login to get a JWT token

```
POST http://<minikube-ip>/api/auth/login
Content-Type: application/json

{
  "userId": "ADMIN001",
  "password": "admin123"
}
```

Copy the token from the response and click **Authorize** in Swagger UI.

---

## Useful Debugging Commands

### See all resources in the namespace

```bash
kubectl get all -n its
```

### Describe a pod (use when pod is stuck or crashing)

```bash
kubectl describe pod <pod-name> -n its
```

Look for the `Events` section at the bottom — it tells you exactly what went wrong.

### Read pod logs

```bash
kubectl logs <pod-name> -n its

# Stream live logs
kubectl logs -f <pod-name> -n its
```

### Get a shell inside a running pod (like SSH)

```bash
kubectl exec -it <pod-name> -n its -- /bin/sh
```

### Check HPA auto-scaler status

```bash
kubectl get hpa -n its
```

### Check what Flyway migrations ran

```bash
# Get a shell inside the postgres pod
kubectl exec -it <postgres-pod-name> -n its -- psql -U postgres -d its_db

# Inside psql:
SELECT * FROM flyway_schema_history;
```

---

## Tear Down (Clean Up)

When you are done learning and want to free up resources:

### Delete just the ITS app (keep Minikube running)

```bash
kubectl delete namespace its
```

This deletes everything inside the `its` namespace — all pods, services, deployments, PVC.

### Stop Minikube (keeps the cluster, frees CPU/RAM)

```bash
minikube stop
```

### Delete Minikube entirely (full reset)

```bash
minikube delete
```

---

## Common Errors and Fixes

| Error | Cause | Fix |
|-------|-------|-----|
| `ImagePullBackOff` | K8s cannot find the image | Did you run `eval $(minikube docker-env)` before building? |
| `CrashLoopBackOff` | Pod keeps crashing on startup | Run `kubectl logs <pod-name> -n its` to see the error |
| `Pending` (never starts) | Not enough CPU/RAM | Run `minikube start --cpus=4 --memory=4096` |
| `Connection refused` on app | DB not ready yet | Check postgres pod is `1/1 Running` first |
| `Flyway migration failed` | Schema mismatch | Check `kubectl logs` on the app pod for SQL errors |

---

## Seed User Credentials

Use these to log in after deployment:

| User ID  | Password | Role       |
|----------|----------|------------|
| ADMIN001 | admin123 | ADMIN      |
| TECH001  | tech123  | TECH_PANEL |
| TECH002  | tech123  | TECH_PANEL |
| HR001    | hr123    | HR_PANEL   |
| HR002    | hr123    | HR_PANEL   |

---

## Full Command Sequence (Quick Reference)

```bash
# 1. Start cluster
minikube start

# 2. Point Docker to Minikube (run in every new terminal)
eval $(minikube docker-env)                    # Mac/Linux
minikube docker-env | Invoke-Expression        # Windows PowerShell

# 3. Build JAR
mvn clean package -DskipTests

# 4. Build Docker image
docker build -t its-app:2.0.0 .

# 5. Deploy to K8s
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/secret.yaml
kubectl apply -f k8s/postgres/
kubectl wait --for=condition=ready pod -l app=its-postgres -n its --timeout=60s
kubectl apply -f k8s/app/

# 6. Watch pods
kubectl get pods -n its -w

# 7. Open in browser
minikube service its-app-service -n its
```

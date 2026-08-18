# DEPLOYMENT.md

## RedeemWise – Reward Points Redemption Optimizer

### Document Version
| Version | Date | Author | Description |
|---------|------|--------|-------------|
| 1.0 | 2024-01-15 | RedeemWise Team | Initial deployment document |

---

## 1. Purpose

This document defines the complete deployment architecture, procedures, and operations for the RedeemWise system. It serves as the primary reference for deploying, monitoring, and maintaining the system in various environments.

---

## 2. Deployment Environments

### 2.1 Environment Overview

| Environment | Purpose | URL | Database |
|-------------|---------|-----|----------|
| **Development** | Local development | localhost | Local MySQL |
| **Testing** | Automated testing | test.redeemwise.com | Test MySQL |
| **Staging** | Pre-production validation | staging.redeemwise.com | Staging MySQL |
| **Production** | Live system | redeemwise.com | Production MySQL |

### 2.2 Environment Configuration

| Aspect | Development | Testing | Staging | Production |
|--------|-------------|---------|---------|------------|
| **Server** | Local machine | Docker containers | Cloud VM | Cloud VMs |
| **Database** | Local MySQL | Docker MySQL | Managed MySQL | Managed MySQL |
| **Scaling** | Single instance | Single instance | Single instance | Multiple instances |
| **Monitoring** | Logs only | Basic metrics | Full monitoring | Full monitoring |
| **Backup** | None | Daily | Daily | Real-time replication |

---

## 3. Deployment Architecture

### 3.1 High-Level Deployment Diagram

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         DEPLOYMENT ARCHITECTURE                             │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │                    LOAD BALANCER (Nginx)                            │   │
│  │                    (Port: 80/443)                                   │   │
│  │                    SSL Termination                                  │   │
│  │                    Rate Limiting                                    │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                    │                                        │
│                                    ▼                                        │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │                    APPLICATION SERVERS                              │   │
│  │                                                                     │   │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐                │   │
│  │  │   Server 1  │  │   Server 2  │  │   Server 3  │                │   │
│  │  │             │  │             │  │             │                │   │
│  │  │ Gateway     │  │ Gateway     │  │ Gateway     │                │   │
│  │  │ Auth        │  │ Auth        │  │ Auth        │                │   │
│  │  │ Card        │  │ Card        │  │ Card        │                │   │
│  │  │ Reward      │  │ Reward      │  │ Reward      │                │   │
│  │  │ Recommend   │  │ Recommend   │  │ Recommend   │                │   │
│  │  │ Eureka      │  │ Eureka      │  │ Eureka      │                │   │
│  │  │ Frontend    │  │ Frontend    │  │ Frontend    │                │   │
│  │  └─────────────┘  └─────────────┘  └─────────────┘                │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                    │                                        │
│                                    ▼                                        │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │                    DATABASE CLUSTER                                 │   │
│  │                                                                     │   │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐                │   │
│  │  │   Primary   │  │  Replica 1  │  │  Replica 2  │                │   │
│  │  │   (Write)   │  │   (Read)    │  │   (Read)    │                │   │
│  │  └─────────────┘  └─────────────┘  └─────────────┘                │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                                                             │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │                    MONITORING & LOGGING                             │   │
│  │                                                                     │   │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐                │   │
│  │  │  Prometheus │  │   Grafana   │  │   ELK Stack │                │   │
│  │  │  (Metrics)  │  │ (Dashboard) │  │   (Logs)    │                │   │
│  │  └─────────────┘  └─────────────┘  └─────────────┘                │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 3.2 Service Deployment Matrix

| Service | Port | Instances | Memory | CPU |
|---------|------|-----------|--------|-----|
| Eureka Server | 8761 | 1 | 512MB | 0.5 vCPU |
| API Gateway | 8080 | 2 | 1GB | 1 vCPU |
| Auth Service | 8081 | 2 | 1GB | 1 vCPU |
| Card Service | 8082 | 2 | 1GB | 1 vCPU |
| Reward Service | 8083 | 2 | 1GB | 1 vCPU |
| Recommendation Service | 8084 | 2 | 1GB | 1 vCPU |
| Frontend | 3000 | 2 | 512MB | 0.5 vCPU |
| MySQL | 3306 | 1 primary + 2 replicas | 4GB | 2 vCPU |

---

## 4. Docker Deployment

### 4.1 Docker Compose Configuration

```yaml
# docker-compose.yml
version: '3.8'

services:
  # Infrastructure Services
  eureka-server:
    build: ./eureka-server
    ports:
      - "8761:8761"
    environment:
      - SPRING_PROFILES_ACTIVE=docker
    networks:
      - redeemwise-network
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8761/actuator/health"]
      interval: 30s
      timeout: 10s
      retries: 3

  api-gateway:
    build: ./api-gateway
    ports:
      - "8080:8080"
    environment:
      - EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka-server:8761/eureka
      - SPRING_PROFILES_ACTIVE=docker
    depends_on:
      eureka-server:
        condition: service_healthy
    networks:
      - redeemwise-network
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health"]
      interval: 30s
      timeout: 10s
      retries: 3

  # Business Services
  auth-service:
    build: ./auth-service
    ports:
      - "8081:8081"
    environment:
      - EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka-server:8761/eureka
      - SPRING_DATASOURCE_URL=jdbc:mysql://mysql-auth:3306/redeemwise_auth
      - SPRING_DATASOURCE_USERNAME=root
      - SPRING_DATASOURCE_PASSWORD=${MYSQL_ROOT_PASSWORD}
      - JWT_SECRET=${JWT_SECRET}
      - SPRING_PROFILES_ACTIVE=docker
    depends_on:
      eureka-server:
        condition: service_healthy
      mysql-auth:
        condition: service_healthy
    networks:
      - redeemwise-network

  card-service:
    build: ./card-service
    ports:
      - "8082:8082"
    environment:
      - EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka-server:8761/eureka
      - SPRING_DATASOURCE_URL=jdbc:mysql://mysql-card:3306/redeemwise_card
      - SPRING_DATASOURCE_USERNAME=root
      - SPRING_DATASOURCE_PASSWORD=${MYSQL_ROOT_PASSWORD}
      - SPRING_PROFILES_ACTIVE=docker
    depends_on:
      eureka-server:
        condition: service_healthy
      mysql-card:
        condition: service_healthy
    networks:
      - redeemwise-network

  reward-service:
    build: ./reward-service
    ports:
      - "8083:8083"
    environment:
      - EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka-server:8761/eureka
      - SPRING_DATASOURCE_URL=jdbc:mysql://mysql-reward:3306/redeemwise_reward
      - SPRING_DATASOURCE_USERNAME=root
      - SPRING_DATASOURCE_PASSWORD=${MYSQL_ROOT_PASSWORD}
      - SPRING_PROFILES_ACTIVE=docker
    depends_on:
      eureka-server:
        condition: service_healthy
      mysql-reward:
        condition: service_healthy
    networks:
      - redeemwise-network

  recommendation-service:
    build: ./recommendation-service
    ports:
      - "8084:8084"
    environment:
      - EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka-server:8761/eureka
      - SPRING_DATASOURCE_URL=jdbc:mysql://mysql-recommendation:3306/redeemwise_recommendation
      - SPRING_DATASOURCE_USERNAME=root
      - SPRING_DATASOURCE_PASSWORD=${MYSQL_ROOT_PASSWORD}
      - SPRING_PROFILES_ACTIVE=docker
    depends_on:
      eureka-server:
        condition: service_healthy
      mysql-recommendation:
        condition: service_healthy
    networks:
      - redeemwise-network

  # Frontend
  frontend:
    build: ./frontend
    ports:
      - "3000:80"
    environment:
      - REACT_APP_API_URL=http://api-gateway:8080
    depends_on:
      - api-gateway
    networks:
      - redeemwise-network

  # Databases
  mysql-auth:
    image: mysql:8.0
    ports:
      - "3307:3306"
    environment:
      - MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD}
      - MYSQL_DATABASE=redeemwise_auth
    volumes:
      - mysql-auth-data:/var/lib/mysql
    networks:
      - redeemwise-network
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      interval: 10s
      timeout: 5s
      retries: 5

  mysql-card:
    image: mysql:8.0
    ports:
      - "3308:3306"
    environment:
      - MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD}
      - MYSQL_DATABASE=redeemwise_card
    volumes:
      - mysql-card-data:/var/lib/mysql
    networks:
      - redeemwise-network
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      interval: 10s
      timeout: 5s
      retries: 5

  mysql-reward:
    image: mysql:8.0
    ports:
      - "3309:3306"
    environment:
      - MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD}
      - MYSQL_DATABASE=redeemwise_reward
    volumes:
      - mysql-reward-data:/var/lib/mysql
    networks:
      - redeemwise-network
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      interval: 10s
      timeout: 5s
      retries: 5

  mysql-recommendation:
    image: mysql:8.0
    ports:
      - "3310:3306"
    environment:
      - MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD}
      - MYSQL_DATABASE=redeemwise_recommendation
    volumes:
      - mysql-recommendation-data:/var/lib/mysql
    networks:
      - redeemwise-network
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      interval: 10s
      timeout: 5s
      retries: 5

networks:
  redeemwise-network:
    driver: bridge

volumes:
  mysql-auth-data:
  mysql-card-data:
  mysql-reward-data:
  mysql-recommendation-data:
```

### 4.2 Dockerfile Example (Auth Service)

```dockerfile
# auth-service/Dockerfile

# Build stage
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### 4.3 Docker Commands

```bash
# Start all services
docker-compose up -d

# Build and start
docker-compose up -d --build

# View logs
docker-compose logs -f auth-service

# Stop all services
docker-compose down

# Stop and remove volumes
docker-compose down -v

# Scale a service
docker-compose up -d --scale auth-service=3

# Check service health
docker-compose ps
```

---

## 5. Kubernetes Deployment (Production)

### 5.1 Kubernetes Manifests

#### 5.1.1 Deployment

```yaml
# k8s/auth-service-deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: auth-service
  namespace: redeemwise
  labels:
    app: auth-service
spec:
  replicas: 3
  selector:
    matchLabels:
      app: auth-service
  template:
    metadata:
      labels:
        app: auth-service
    spec:
      containers:
        - name: auth-service
          image: redeemwise/auth-service:latest
          ports:
            - containerPort: 8081
          env:
            - name: SPRING_PROFILES_ACTIVE
              value: "production"
            - name: EUREKA_CLIENT_SERVICEURL_DEFAULTZONE
              valueFrom:
                configMapKeyRef:
                  name: app-config
                  key: eureka-url
            - name: JWT_SECRET
              valueFrom:
                secretKeyRef:
                  name: app-secrets
                  key: jwt-secret
          resources:
            requests:
              memory: "512Mi"
              cpu: "500m"
            limits:
              memory: "1Gi"
              cpu: "1000m"
          readinessProbe:
            httpGet:
              path: /actuator/health/readiness
              port: 8081
            initialDelaySeconds: 30
            periodSeconds: 10
          livenessProbe:
            httpGet:
              path: /actuator/health/liveness
              port: 8081
            initialDelaySeconds: 60
            periodSeconds: 10
```

#### 5.1.2 Service

```yaml
# k8s/auth-service-service.yaml
apiVersion: v1
kind: Service
metadata:
  name: auth-service
  namespace: redeemwise
spec:
  selector:
    app: auth-service
  ports:
    - protocol: TCP
      port: 8081
      targetPort: 8081
  type: ClusterIP
```

#### 5.1.3 Ingress

```yaml
# k8s/ingress.yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: redeemwise-ingress
  namespace: redeemwise
  annotations:
    nginx.ingress.kubernetes.io/ssl-redirect: "true"
    nginx.ingress.kubernetes.io/use-regex: "true"
    cert-manager.io/cluster-issuer: "letsencrypt-prod"
spec:
  tls:
    - hosts:
        - api.redeemwise.com
        - redeemwise.com
      secretName: redeemwise-tls
  rules:
    - host: api.redeemwise.com
      http:
        paths:
          - path: /api/auth
            pathType: Prefix
            backend:
              service:
                name: auth-service
                port:
                  number: 8081
          - path: /api/cards
            pathType: Prefix
            backend:
              service:
                name: card-service
                port:
                  number: 8082
          - path: /api/rewards
            pathType: Prefix
            backend:
              service:
                name: reward-service
                port:
                  number: 8083
          - path: /api/recommendations
            pathType: Prefix
            backend:
              service:
                name: recommendation-service
                port:
                  number: 8084
    - host: redeemwise.com
      http:
        paths:
          - path: /
            pathType: Prefix
            backend:
              service:
                name: frontend-service
                port:
                  number: 3000
```

---

## 6. CI/CD Pipeline

### 6.1 GitHub Actions Workflow

```yaml
# .github/workflows/deploy.yml
name: Build and Deploy

on:
  push:
    branches: [main, develop]
  pull_request:
    branches: [main]

env:
  REGISTRY: ghcr.io
  IMAGE_NAME: ${{ github.repository }}

jobs:
  test:
    runs-on: ubuntu-latest
    services:
      mysql:
        image: mysql:8.0
        env:
          MYSQL_ROOT_PASSWORD: root
          MYSQL_DATABASE: redeemwise_test
        ports:
          - 3306:3306
        options: >-
          --health-cmd="mysqladmin ping"
          --health-interval=10s
          --health-timeout=5s
          --health-retries=5
    
    steps:
      - uses: actions/checkout@v3
      
      - name: Set up JDK 21
        uses: actions/setup-java@v3
        with:
          java-version: '21'
          distribution: 'temurin'
      
      - name: Build and Test
        run: mvn clean verify
      
      - name: Upload Coverage
        uses: codecov/codecov-action@v3

  build:
    needs: test
    runs-on: ubuntu-latest
    if: github.event_name == 'push'
    
    steps:
      - uses: actions/checkout@v3
      
      - name: Log in to Container Registry
        uses: docker/login-action@v2
        with:
          registry: ${{ env.REGISTRY }}
          username: ${{ github.actor }}
          password: ${{ secrets.GITHUB_TOKEN }}
      
      - name: Build and push Docker images
        run: |
          for service in auth-service card-service reward-service recommendation-service api-gateway; do
            docker build -t ${{ env.REGISTRY }}/${{ env.IMAGE_NAME }}/$service:latest ./$service
            docker push ${{ env.REGISTRY }}/${{ env.IMAGE_NAME }}/$service:latest
          done

  deploy-staging:
    needs: build
    runs-on: ubuntu-latest
    if: github.ref == 'refs/heads/develop'
    
    steps:
      - name: Deploy to Staging
        run: |
          kubectl set image deployment/auth-service \
            auth-service=${{ env.REGISTRY }}/${{ env.IMAGE_NAME }}/auth-service:latest \
            --namespace=staging
          kubectl set image deployment/card-service \
            card-service=${{ env.REGISTRY }}/${{ env.IMAGE_NAME }}/card-service:latest \
            --namespace=staging

  deploy-production:
    needs: build
    runs-on: ubuntu-latest
    if: github.ref == 'refs/heads/main'
    environment: production
    
    steps:
      - name: Deploy to Production
        run: |
          kubectl set image deployment/auth-service \
            auth-service=${{ env.REGISTRY }}/${{ env.IMAGE_NAME }}/auth-service:latest \
            --namespace=production
          kubectl set image deployment/card-service \
            card-service=${{ env.REGISTRY }}/${{ env.IMAGE_NAME }}/card-service:latest \
            --namespace=production
```

### 6.2 Pipeline Stages

| Stage | Description | Duration | Success Criteria |
|-------|-------------|----------|------------------|
| **Checkout** | Pull code from repository | 1 min | Code available |
| **Build** | Compile Java code | 3 min | No compilation errors |
| **Test** | Run unit tests | 5 min | All tests pass |
| **Integration Test** | Run integration tests | 10 min | All tests pass |
| **Security Scan** | Scan for vulnerabilities | 5 min | No critical vulnerabilities |
| **Build Docker** | Create Docker images | 5 min | Images built successfully |
| **Push Images** | Push to container registry | 3 min | Images pushed |
| **Deploy to Staging** | Deploy to staging environment | 5 min | Deployment successful |
| **Smoke Tests** | Run basic smoke tests | 3 min | All smoke tests pass |
| **Deploy to Production** | Deploy to production | 5 min | Deployment successful |

---

## 7. Monitoring and Observability

### 7.1 Monitoring Stack

| Component | Purpose | Port |
|-----------|---------|------|
| **Prometheus** | Metrics collection | 9090 |
| **Grafana** | Metrics visualization | 3000 |
| **ELK Stack** | Log aggregation | 5601 |
| **Jaeger** | Distributed tracing | 16686 |

### 7.2 Health Check Endpoints

| Service | Endpoint | Description |
|---------|----------|-------------|
| Auth Service | `/actuator/health` | Service health status |
| Card Service | `/actuator/health` | Service health status |
| Reward Service | `/actuator/health` | Service health status |
| Recommendation Service | `/actuator/health` | Service health status |
| Gateway | `/actuator/health` | Gateway health status |
| Eureka | `/actuator/health` | Eureka health status |

### 7.3 Metrics to Monitor

| Category | Metric | Alert Threshold |
|----------|--------|-----------------|
| **Availability** | Service uptime | < 99.5% |
| **Performance** | Response time (p95) | > 500ms |
| **Performance** | Throughput (req/s) | < 50 req/s |
| **Errors** | Error rate | > 1% |
| **Resources** | CPU usage | > 80% |
| **Resources** | Memory usage | > 85% |
| **Database** | Connection pool usage | > 80% |
| **Database** | Query execution time | > 100ms |

### 7.4 Alerting Rules

```yaml
# prometheus/alerts.yml
groups:
  - name: redeemwise-alerts
    rules:
      - alert: ServiceDown
        expr: up{job="redeemwise"} == 0
        for: 1m
        labels:
          severity: critical
        annotations:
          summary: "Service {{ $labels.instance }} is down"
          
      - alert: HighErrorRate
        expr: rate(http_requests_total{status=~"5.."}[5m]) > 0.01
        for: 5m
        labels:
          severity: warning
        annotations:
          summary: "High error rate on {{ $labels.instance }}"
          
      - alert: HighResponseTime
        expr: histogram_quantile(0.95, rate(http_request_duration_seconds_bucket[5m])) > 0.5
        for: 5m
        labels:
          severity: warning
        annotations:
          summary: "High response time on {{ $labels.instance }}"
```

---

## 8. Backup and Recovery

### 8.1 Backup Strategy

| Component | Backup Type | Frequency | Retention | Storage |
|-----------|-------------|-----------|-----------|---------|
| **MySQL** | Full backup | Daily | 30 days | S3 |
| **MySQL** | Binary logs | Real-time | 7 days | S3 |
| **Application Config** | Git repository | On change | Indefinite | GitHub |
| **Docker Images** | Container registry | On build | 30 days | GHCR |

### 8.2 Backup Scripts

```bash
#!/bin/bash
# scripts/backup-database.sh

BACKUP_DIR="/backups/mysql"
DATE=$(date +%Y%m%d_%H%M%S)
DB_NAME="redeemwise"

# Full backup
mysqldump -u root -p$MYSQL_ROOT_PASSWORD \
  --all-databases \
  --single-transaction \
  --routines \
  --triggers \
  | gzip > "$BACKUP_DIR/full_backup_$DATE.sql.gz"

# Upload to S3
aws s3 cp "$BACKUP_DIR/full_backup_$DATE.sql.gz" \
  s3://redeemwise-backups/mysql/

# Cleanup old backups (keep 30 days)
find "$BACKUP_DIR" -name "*.sql.gz" -mtime +30 -delete
```

### 8.3 Recovery Procedures

| Scenario | Recovery Time | Procedure |
|----------|---------------|-----------|
| **Data Corruption** | < 1 hour | Restore from latest backup |
| **Accidental Deletion** | < 30 min | Point-in-time recovery using binary logs |
| **Database Failure** | < 5 min | Failover to replica |
| **Complete Loss** | < 4 hours | Full restore from S3 backup |

---

## 9. Security Considerations

### 9.1 Security Checklist

| Category | Item | Status |
|----------|------|--------|
| **Authentication** | JWT tokens with expiry | ✅ |
| **Authorization** | Role-based access control | ✅ |
| **Encryption** | HTTPS enabled | ✅ |
| **Encryption** | Database encryption at rest | ✅ |
| **Secrets** | No hardcoded secrets | ✅ |
| **Secrets** | Use environment variables | ✅ |
| **Network** | Firewall rules configured | ✅ |
| **Network** | VPC isolation | ✅ |
| **Dependencies** | No known vulnerabilities | ✅ |
| **Logging** | Security events logged | ✅ |

### 9.2 Secret Management

| Secret | Location | Rotation |
|--------|----------|----------|
| JWT Secret | Environment variable | Every 90 days |
| Database Password | Kubernetes Secret | Every 90 days |
| API Keys | Environment variable | As needed |
| SSL Certificates | Cert-Manager | Auto-renewal |

---

## 10. Rollback Procedures

### 10.1 Rollback Strategy

| Scenario | Rollback Method | Time |
|----------|-----------------|------|
| **Code Issue** | Redeploy previous version | 5 min |
| **Database Issue** | Restore from backup | 30 min |
| **Configuration Issue** | Revert config change | 2 min |
| **Full Rollback** | Restore entire system | 1 hour |

### 10.2 Rollback Commands

```bash
# Kubernetes rollback
kubectl rollout undo deployment/auth-service -n production

# Docker Compose rollback
docker-compose down
git checkout v1.0.0
docker-compose up -d --build

# Database rollback
mysql -u root -p < restore_backup.sql
```

---

## 11. Troubleshooting

### 11.1 Common Issues

| Issue | Symptoms | Solution |
|-------|----------|----------|
| **Service Won't Start** | 503 errors | Check logs, verify dependencies |
| **Connection Refused** | Cannot connect to DB | Check DB status, credentials |
| **High Memory Usage** | Slow response, OOM | Check for memory leaks, scale up |
| **High CPU Usage** | Slow response | Check for infinite loops, scale up |
| **JWT Token Invalid** | 401 errors | Check token expiry, secret key |

### 11.2 Debug Commands

```bash
# Check service status
kubectl get pods -n production

# View service logs
kubectl logs -f deployment/auth-service -n production

# Port forward for debugging
kubectl port-forward service/auth-service 8081:8081 -n production

# Check database connectivity
mysql -h mysql-primary -u root -p -e "SELECT 1"

# Check Eureka registration
curl http://eureka-server:8761/eureka/apps
```

---

## 12. Scaling

### 12.1 Horizontal Scaling

| Service | Current | Max | Scaling Trigger |
|---------|---------|-----|-----------------|
| Auth Service | 2 | 10 | CPU > 70% |
| Card Service | 2 | 10 | CPU > 70% |
| Reward Service | 2 | 10 | CPU > 70% |
| Recommendation Service | 2 | 10 | CPU > 70% |
| API Gateway | 2 | 5 | CPU > 70% |

### 12.2 Vertical Scaling

| Component | Current | Max | When to Scale |
|-----------|---------|-----|---------------|
| Application Pods | 1GB RAM | 4GB RAM | Memory pressure |
| MySQL Primary | 4GB RAM | 16GB RAM | Query performance |

### 12.3 Auto-scaling Configuration

```yaml
# k8s/hpa.yaml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: auth-service-hpa
  namespace: production
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: auth-service
  minReplicas: 2
  maxReplicas: 10
  metrics:
    - type: Resource
      resource:
        name: cpu
        target:
          type: Utilization
          averageUtilization: 70
    - type: Resource
      resource:
        name: memory
        target:
          type: Utilization
          averageUtilization: 80
```

---

*Document maintained by RedeemWise Development Team*
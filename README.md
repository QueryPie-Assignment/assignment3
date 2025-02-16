# Query-Pie CI/CD

## 📌 개요
Query-Pie 프로젝트의 CI/CD 파이프라인은 **GitHub Actions**와 **Docker**를 활용하여 개발 및 운영 환경을 자동화하였습니다.  
이를 통해 코드 변경 사항을 신속하고 안정적으로 배포할 수 있도록 구성되었습니다.

## 🚀 주요 기능
- **GitHub Actions 기반 CI/CD 자동화**
  - `develop` 브랜치 → 개발 서버 배포
  - `master` 브랜치 → 운영 서버 배포
- **Docker 컨테이너 기반 운영**
  - MySQL 및 Redis 데이터 관리
  - Prometheus & Grafana를 이용한 모니터링 환경 구축
- **자동화된 환경 설정**
  - GitHub Secrets를 활용한 환경 변수 관리 (`application.yml`)
  - SSH를 이용한 EC2 서버 배포 자동화

---
<br>

## 🔧 1. CI/CD 파이프라인

### 1.1 GitHub Actions Workflow
CI/CD 파이프라인은 `.github/workflows/gradle.yml`에 정의되어 있습니다.

### 1.2 트리거 이벤트
| 이벤트 | 동작 |
|--------|------|
| `develop` 브랜치 푸시 | **개발 서버 배포** |
| `master` 브랜치 PR 머지 | **운영 서버 배포** |
| `workflow_dispatch` | **수동 배포 실행 가능** |

### 1.3 빌드 및 배포 과정
1. **GitHub Actions 실행**
   - 레포지토리 체크아웃 및 JDK 17 설정
   - 환경별 YML 파일 생성 (`application.yml`)
   - Prometheus 설정 파일 생성
   - Gradle을 이용한 빌드 (`./gradlew clean build`)
   
2. **Docker 이미지 빌드 및 배포**
   - DockerHub 로그인
   - 개발/운영 환경에 맞는 Docker 이미지 빌드 및 푸시

3. **EC2 서버 배포**
   - SSH 키를 이용해 EC2 서버 접속
   - 기존 컨테이너 종료 및 제거
   - 최신 Docker 이미지 풀링 및 실행

---
<br>

## 🐳 2. Docker 환경 구성

### 2.1 애플리케이션 컨테이너 (`Dockerfile`)
```dockerfile
FROM openjdk:17
ARG JAR_FILE=presentation/build/libs/*.jar
ARG DEBIAN_FRONTEND=noninteractive
ENV TZ=Asia/Seoul
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

### 2.2 MySQL 및 Redis 설정 (`docker-compose.yml`)
```docker-compose
version: '3.7'

services:
  mysql:
    container_name: mysql
    image: mysql:8.0
    restart: always
    environment:
      MYSQL_DATABASE: assignment
      MYSQL_ROOT_PASSWORD: assignment1!
      TZ: Asia/Seoul
    ports:
      - 3306:3306
    volumes:
      - ./db/mysql/data:/var/lib/mysql
      - ./db/mysql/init:/docker-entrypoint-initdb.d
    networks:
      - docker-bridge

  redis:
    container_name: redis
    image: redis
    restart: always
    environment:
      TZ: Asia/Seoul
    ports:
      - "6379:6379"
    volumes:
      - ./redis/data:/data
      - ./redis/conf/redis.conf:/usr/local/conf/redis.conf
    networks:
      - docker-bridge

networks:
  docker-bridge:
    driver: bridge

```

### 2.3 모니터링 시스템 설정 (`docker-compose.monitoring.yml`)
```docker-compose
version: '3.7'

services:
  prometheus:
    image: prom/prometheus
    container_name: prometheus
    volumes:
      - ./prometheus.yml:/prometheus/prometheus.yml:ro
    ports:
      - 19090:9090
    command:
      - "--web.enable-lifecycle"
    restart: always
    user: root
    networks:
      - promnet

  grafana:
    image: grafana/grafana
    container_name: grafana
    volumes:
      - ./grafana-volume:/var/lib/grafana
    restart: always
    ports:
      - 13030:3000
    user: root
    networks:
      - promnet

  node-exporter:
    image: prom/node-exporter
    container_name: node-exporter
    restart: always
    ports:
      - "9100:9100"
    networks:
      - promnet

networks:
  promnet:
    driver: bridge
```

---
<br>

## 📦 3. 배포 방법

### 3.1 개발 서버 배포 (`develop` 브랜치)
- `develop` 브랜치로 **코드를 푸시**하면 GitHub Actions가 자동으로 실행됩니다.
- GitHub Actions가 실행되면:
  1. 애플리케이션을 빌드 (`./gradlew clean build`)
  2. Docker 이미지를 빌드 후 **Docker Hub에 push**
  3. EC2 서버에 접속하여 기존 컨테이너를 종료하고 새로운 컨테이너 실행

#### 👉 수동 배포 (GitHub Actions 실행)
1. GitHub 레포지토리로 이동
2. **Actions** 탭 클릭
3. `query-pie CI/CD` 워크플로우 실행
4. **개발 서버 배포 완료 🎉**

### 3.2 운영 서버 배포 (`master` 브랜치)
- `master` 브랜치로 PR을 **머지**하면 GitHub Actions가 자동으로 실행됩니다.
- GitHub Actions가 실행되면:
  1. 애플리케이션을 빌드 (`./gradlew clean build`)
  2. Docker 이미지를 빌드 후 **Docker Hub에 push**
  3. EC2 운영 서버에 접속하여 기존 컨테이너를 종료하고 새로운 컨테이너 실행

#### 👉 수동 배포 (GitHub Actions 실행)
1. GitHub 레포지토리로 이동
2. **Actions** 탭 클릭
3. `query-pie CI/CD` 워크플로우 실행
4. **운영 서버 배포 완료 🎉**

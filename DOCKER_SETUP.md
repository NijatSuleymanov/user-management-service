# Docker Compose Qurulması

## 📋 Tələblər

- Docker Desktop yüklü olmalı
- Docker Compose yüklü olmalı (Docker Desktop ilə gəlir)
- Git bash və ya terminal

## 🚀 Addım-Addım Qurulma

### 1. **Build JAR Faylı**

Əvvəl Gradle ilə Spring Boot proyektini build etməlisiniz:

```bash
# Windows
./gradlew clean bootJar

# Linux/Mac
./gradlew clean bootJar
```

Bu əmr `build/libs/user-management-service-0.0.1-SNAPSHOT.jar` yaradacaq.

### 2. **Docker Compose Start**

```bash
docker-compose up -d
```

**Əmrin Mənası:**
- `docker-compose up` - Container-ləri start et
- `-d` - Detached mode (background-da işləsin)

### 3. **Status Yoxla**

```bash
# Bütün container-ləri göstər
docker-compose ps

# Log-ları göstər (PostgreSQL)
docker-compose logs postgres

# Log-ları göstər (App)
docker-compose logs app

# Bütün log-ları izlə
docker-compose logs -f
```

## ✅ Kontrol Edin

### **PostgreSQL Bağlantısı**

```bash
# Container-ə gir
docker-compose exec postgres psql -U postgres -d userdb

# SQL-ləri çalıştır
\dt              # Tablıları göstər
SELECT * FROM users;  # Users table-ini göstər
\q               # Çıx
```

### **API Testi**

```bash
# Server çalışıyor mu?
curl http://localhost:8080/api/users

# User yarad
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "1234567890",
    "role": "USER"
  }'
```

## 🛑 Stop Etmə

```bash
# Container-ləri stop et
docker-compose down

# Container-ləri stop et + volume sil
docker-compose down -v
```

## 📁 Fayllar

```
project-root/
├── Dockerfile           ← Spring Boot container config
├── docker-compose.yml   ← PostgreSQL + App orchestration
├── .dockerignore        ← Docker-a ləzümsüz faylları göstər
└── build/libs/
    └── user-management-service-0.0.1-SNAPSHOT.jar
```

## 🔍 Faydalı əmrlər

```bash
# Container şəkillərini göstər
docker images

# Çalışan container-ləri göstər
docker ps

# Bütün container-ləri göstər (stopped daxil)
docker ps -a

# Container-nin shell-inə gir
docker-compose exec app bash

# Container-nin IP-sini öyrən
docker-compose exec app hostname -I
```

## ⚙️ Konfiquratsiya

### **Portlar**
- PostgreSQL: `5432`
- Spring Boot App: `8080`

### **Environment Variables** (docker-compose.yml-də)
- `POSTGRES_DB`: userdb
- `POSTGRES_USER`: postgres
- `POSTGRES_PASSWORD`: postgres
- `SPRING_DATASOURCE_URL`: jdbc:postgresql://postgres:5432/userdb

### **Volume**
- `postgres_data:/var/lib/postgresql/data` - Verilənlər saxlanılır

## 🐛 Problemlərin Həlli

### **Port Artıq İstifadə Olunur**
```bash
# Port 5432 kə bağlanan prosesi tapa
lsof -i :5432

# Port 8080 kə bağlanan prosesi tapa
lsof -i :8080

# Prosesi qat (PID-ni əvəz et)
kill -9 <PID>
```

### **Database Bağlantısı Xəta**
```bash
# PostgreSQL log-larını yoxla
docker-compose logs postgres

# Container-nin IP-sini yoxla
docker-compose exec app ping postgres
```

### **App Start Xəta**
```bash
# App log-larını göstər
docker-compose logs app

# Build yenidən et
docker-compose up -d --build
```

## 📝 Diaqram

```
┌─────────────────────────────────────────────┐
│         Docker Network (user-network)       │
├─────────────────────────────────────────────┤
│                                             │
│  ┌──────────────────────┐                  │
│  │  PostgreSQL 15       │                  │
│  │  Container           │                  │
│  │  Port: 5432          │                  │
│  │  Database: userdb    │                  │
│  └──────────────────────┘                  │
│            ↑                                │
│            │ (jdbc:postgresql://postgres)  │
│            │                               │
│  ┌──────────────────────┐                  │
│  │  Spring Boot App     │                  │
│  │  Container           │                  │
│  │  Port: 8080          │                  │
│  │  User Management     │                  │
│  └──────────────────────┘                  │
│                                             │
└─────────────────────────────────────────────┘
```

## ✨ Avantajları

- ✅ Həmişə eyni environment
- ✅ Produksiyada da eyni şəkildə işləyəcək
- ✅ Team hamısı eyni quruluma sahib olacaq
- ✅ CI/CD ilə asandır
- ✅ Lokal dev + production əynidir
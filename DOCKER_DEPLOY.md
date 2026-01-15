# 疲劳监测系统 Docker 部署指南

## 系统架构

本系统采用前后端分离架构，使用Docker Compose进行容器化部署。

### 服务组件

1. **MySQL 8.0** - 数据库服务
2. **Spring Boot Backend** - 后端API服务（端口8080）
3. **Redis 7** - 缓存服务（可选）
4. **Nginx** - 反向代理服务（可选）

## 前置要求

- Docker 20.10+
- Docker Compose 2.0+
- 至少4GB可用内存
- 至少10GB可用磁盘空间

## 快速开始

### 1. 克隆项目

```bash
git clone <repository-url>
cd vue-admin-template
```

### 2. 配置环境变量

编辑 `docker-compose.yml` 文件，根据需要修改以下配置：

```yaml
environment:
  # 数据库密码
  MYSQL_ROOT_PASSWORD: root123456
  MYSQL_PASSWORD: fatigue123
  
  # JWT密钥（生产环境请修改）
  JWT_SECRET: your_secret_key_here
  
  # 时区
  TZ: Asia/Shanghai
```

### 3. 构建并启动服务

```bash
# 构建并启动所有服务
docker-compose up -d

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f backend
```

### 4. 初始化数据库

数据库会在首次启动时自动初始化，执行 `backend/src/main/resources/sql/init.sql` 脚本。

### 5. 访问系统

- **后端API**: http://localhost:8080
- **默认管理员账号**: admin / admin123

## 常用命令

### 服务管理

```bash
# 启动所有服务
docker-compose up -d

# 停止所有服务
docker-compose stop

# 重启所有服务
docker-compose restart

# 停止并删除所有容器
docker-compose down

# 停止并删除所有容器及数据卷
docker-compose down -v
```

### 日志查看

```bash
# 查看所有服务日志
docker-compose logs

# 查看后端服务日志
docker-compose logs backend

# 实时查看后端服务日志
docker-compose logs -f backend

# 查看最近100行日志
docker-compose logs --tail=100 backend
```

### 容器管理

```bash
# 进入后端容器
docker-compose exec backend bash

# 进入MySQL容器
docker-compose exec mysql bash

# 连接MySQL数据库
docker-compose exec mysql mysql -ufatigue -pfatigue123 fatigue_monitoring

# 备份数据库
docker-compose exec mysql mysqldump -ufatigue -pfatigue123 fatigue_monitoring > backup.sql

# 恢复数据库
docker-compose exec -T mysql mysql -ufatigue -pfatigue123 fatigue_monitoring < backup.sql
```

## 配置说明

### 后端配置

后端配置主要通过环境变量设置，主要配置项：

| 配置项 | 说明 | 默认值 |
|--------|------|--------|
| SPRING_DATASOURCE_URL | 数据库连接URL | - |
| SPRING_DATASOURCE_USERNAME | 数据库用户名 | fatigue |
| SPRING_DATASOURCE_PASSWORD | 数据库密码 | fatigue123 |
| JWT_SECRET | JWT密钥 | - |
| JWT_EXPIRATION | JWT过期时间（秒） | 86400 |
| SPRING_REDIS_HOST | Redis主机地址 | redis |
| SPRING_REDIS_PORT | Redis端口 | 6379 |

### MySQL配置

| 配置项 | 说明 | 默认值 |
|--------|------|--------|
| MYSQL_ROOT_PASSWORD | root密码 | root123456 |
| MYSQL_DATABASE | 数据库名 | fatigue_monitoring |
| MYSQL_USER | 应用用户名 | fatigue |
| MYSQL_PASSWORD | 应用密码 | fatigue123 |

## 数据持久化

系统使用Docker数据卷进行数据持久化：

- `mysql-data`: MySQL数据
- `redis-data`: Redis数据
- `backend-logs`: 后端日志

数据卷位于Docker默认数据目录，可通过以下命令查看：

```bash
docker volume ls
```

## 性能优化

### 1. JVM参数调优

编辑 `docker-compose.yml` 中的 `JAVA_OPTS` 环境变量：

```yaml
environment:
  JAVA_OPTS: "-Xms1g -Xmx2g -XX:+UseG1GC -XX:MaxGCPauseMillis=200"
```

### 2. 数据库优化

编辑MySQL配置，在 `docker-compose.yml` 中添加：

```yaml
command:
  - --innodb_buffer_pool_size=1G
  - --max_connections=200
  - --query_cache_size=64M
```

### 3. Redis配置

根据实际需求调整Redis内存限制：

```yaml
redis:
  command: redis-server --appendonly yes --maxmemory 256mb --maxmemory-policy allkeys-lru
```

## 监控与维护

### 健康检查

系统已配置健康检查，可通过以下命令查看：

```bash
docker inspect --format='{{.State.Health.Status}}' fatigue-monitoring-backend
```

### 资源监控

```bash
# 查看容器资源使用情况
docker stats

# 查看特定容器资源使用
docker stats fatigue-monitoring-backend
```

### 日志管理

建议配置日志轮转，避免日志文件过大。在 `docker-compose.yml` 中添加：

```yaml
logging:
  driver: "json-file"
  options:
    max-size: "10m"
    max-file: "3"
```

## 故障排查

### 1. 后端服务无法启动

```bash
# 查看后端日志
docker-compose logs backend

# 检查数据库连接
docker-compose exec backend ping mysql
```

### 2. 数据库连接失败

```bash
# 检查MySQL服务状态
docker-compose ps mysql

# 查看MySQL日志
docker-compose logs mysql

# 测试数据库连接
docker-compose exec mysql mysql -ufatigue -pfatigue123 -e "SELECT 1"
```

### 3. 端口冲突

如果端口被占用，修改 `docker-compose.yml` 中的端口映射：

```yaml
ports:
  - "8081:8080"  # 将外部端口改为8081
```

### 4. 内存不足

增加Docker可用内存或调整JVM参数：

```yaml
environment:
  JAVA_OPTS: "-Xms256m -Xmx512m"
```

## 生产环境部署建议

1. **安全性**
   - 修改所有默认密码
   - 使用强JWT密钥
   - 配置HTTPS（使用Nginx）
   - 限制数据库外部访问

2. **性能**
   - 使用外部MySQL和Redis服务
   - 配置负载均衡
   - 启用Redis缓存
   - 优化数据库索引

3. **监控**
   - 集成Prometheus和Grafana
   - 配置日志收集（ELK）
   - 设置告警通知

4. **备份**
   - 定期备份数据库
   - 备份配置文件
   - 制定灾难恢复计划

## 升级部署

```bash
# 1. 备份数据
docker-compose exec mysql mysqldump -ufatigue -pfatigue123 fatigue_monitoring > backup.sql

# 2. 拉取最新代码
git pull

# 3. 重新构建并启动
docker-compose up -d --build

# 4. 查看服务状态
docker-compose ps
```

## 技术支持

如有问题，请联系技术支持团队或查看项目文档。

---

**注意**: 生产环境部署前，请务必修改所有默认密码和密钥，并进行充分测试。

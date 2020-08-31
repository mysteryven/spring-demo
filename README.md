# Spring-boot 博客

## 准备

如果想成功启动此项目，你需要做到下面三个条件：

1. 安装 Docker
2. 配置 Java 环境（Java，Maven）
3. 配置 Node 环境（Node）

## 启动 

### 启动数据库

```bash
    docker run --name my-sql -d -e MYSQL_ROOT_PASSWORD=my-secret--pw--pw -p 3306:3306 -v /Users/wenzhe/java-repos/demo1/target/mysql:/var/lib/mysql mysql
```
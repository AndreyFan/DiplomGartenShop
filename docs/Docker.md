
```yaml
version: "3"
services:
  mysqldb:
    container_name: mysql-shop
    image: mysql
    ports:
      - "23306:3306"
    environment:
      MYSQL_DATABASE: gartenshop
      MYSQL_ROOT_PASSWORD: 220606
    healthcheck:
      test: [ "CMD-SHELL", "mysqladmin ping -h localhost -u root -p'220606'" ]
      timeout: 20s
      retries: 10

  spring-boot-app:
    container_name: spring-online-shop
    image: gartenshop.1.0
    restart: on-failure
    build:
      context: .
      dockerfile: Dockerfile
    ports:
      - "18088:8088"
    environment:
      MYSQL_HOST: host.docker.internal
      MYSQL_DB_NAME: gartenshop
      MYSQL_USER: root
      MYSQL_PASSWORD: 220606
      MYSQL_PORT: 23306    ## we are in same network so_ we connect internally.
    depends_on:
      mysqldb:
        condition: service_healthy
```


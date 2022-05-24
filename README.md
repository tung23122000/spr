# Spring Boot Application - SPR Config API

[![Build Status](https://travis-ci.org/codecentric/springboot-sample-app.svg?branch=master)](https://travis-ci.org/codecentric/springboot-sample-app)
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

## Thông tin của project

##### - SPR Config API đảm nhận việc khai báo các thông tin đấu nối dành cho quản trị viên của MBF

##### - Tên service web-api

##### - Port sử dụng 9001

## Requirements

For building and running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 3](https://maven.apache.org)

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method
in the `dts.com.vn.SprConfigApiApplication` class from your IDE.

Alternatively you can use
the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html)
like so:

## Project have 4 environment configuration

* **application**: File cấu hình chung
* **application-local**:      File cấu hình để chạy project ở local kết nối đến DB production
* **application-testbed**:    File cấu hình để build project for TESTBED server
* **application-prod**:       File cấu hình để build project for PRODUCTION server

### Run project with command line

````shell
mvn spring-boot:run
````

## Package the project

````shell
mvn package -DskipTests=true
````

## Deploy project to server

1. Sửa file **application.yml** thuộc tính spring.active.profile ứng với môi trường cần deploy
2. Sau đó thực hiện đóng gói project theo câu lệnh trên
3. Copy file .jar của project lên server vào đường dẫn /web-admin/api
4. SSH đến server cần deploy và run ứng dụng dưới dạng 1 service của hệ điều hành bằng command sau

````shell
service web-api restart
````

5. Kiểm tra tình trạng của service bằng 1 trong 2 command dưới đây

````shell
journalctl -u web-api -f

service web-api status
````

> <span style="color:red">Chú ý khi deploy trên production</span>
> 1. Tiền hành deloy trên server 135 trước.
> 2. Tiến hành tắt service ở server 138 để request đẩy sang con 135.
> 3. Tiến hành kiểm tra nếu server 135 chạy không lỗi thì deploy lên server 138.
> 4. Nếu server 135 chạy xảy ra vấn đề thì tiến hành rollback bằng cách bật lại service trên server 138.

## Copyright

Released under the Apache License 2.0. See
the [LICENSE](https://github.com/codecentric/springboot-sample-app/blob/master/LICENSE) file.
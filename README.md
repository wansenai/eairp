<h1 align="center">WanSen ERP</h1>
<div align="center">
 <strong>
  Next generation artificial intelligent ERP system
 </strong>
</div>
<br />

<div align="center">
  <!-- Crates version -->
  <a href="https://spring.io/projects/spring-boot#learn">
    <img alt="Static Badge" src="https://img.shields.io/badge/spring-boot?label=Spring%20Boot%203.1.3">
  </a>
  <a href="#">
    <img alt="GitHub Workflow Status (with event)" src="https://img.shields.io/github/actions/workflow/status/wansenai/wansenerp/maven.yml">
  </a>
  <!-- commits -->
  <a href="#">
    <img alt="GitHub last commit (branch)" src="https://img.shields.io/github/last-commit/wansenai/wansenerp/master">
  </a>
</div>
<br />

On the basis of ERP business, we have expanded GPT-3.5. individually or company can fine tune your model through our system and deploy it through Docker or other methods. 

You can provide fully automated business form submission operations through your simple description, and you can chat, interact, and consult information with GPT.

**Note: This is a front-end and back-end separated project. This repository stores backend code, please browse [here](https://github.com/wansenai/wansenerpui) for the front-end warehouse.**

[Enginsh](https://github.com/wansenai/wansen-erp/blob/master/README.md) / [简体中文](https://github.com/wansenai/wansen-erp/blob/master/README_ZH.md)

## Online preview

We have deployed this program on Amazon Cloud for you to experience before deciding to use it. 
If you have any questions or suggestions, please submit [issue](https://github.com/wansenai/WansenERP/issues/new) and we will handle them in a timely manner.

preview address: http://erp.wansen.cloud/

## Quick Start

You need to first install the Docker environment and already have the Redis and MySQL8.0 environments

### Pull images
```shell
docker pull wansenai/erp-core:2.0.1
```
### Run Server
You can customize and modify port 9998, please ensure that the service ports monitored by your front-end are consistent
```shell
docker run -d -p 9998:8088 erp-core:2.0.1 --name wansenerp 
```

## Version
1. 1.0.2 (current version)
2. 1.0.1 [old versions](https://github.com/wansenai/wansen-erp/releases/tag/V1.0.1)

## Module Description

1. **Domain Module:**

Business Operation Data Object.
```xml
<dependency>
    <groupId>com.wansensoft</groupId>
    <artifactId>domain</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

2. **Dao Module:**

Data Access Objects separate data access logic from business logic to provide access and operations to data persistent storage.
```xml
<dependency>
    <groupId>com.wansensoft</groupId>
    <artifactId>dao</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

3. **Service Module:**

Business logic layer, which delegates specific business logic processing to the Service layer.
```xml
<dependency>
    <groupId>com.wansensoft</groupId>
    <artifactId>service</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

4. **Utils Module:**

Tool class code that encapsulates some specific operations.
```xml
<dependency>
    <groupId>com.wansensoft</groupId>
    <artifactId>utils</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

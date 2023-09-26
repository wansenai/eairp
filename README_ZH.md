<h1 align="center">WanSen ERP</h1>
<div align="center">
 <strong>
  下一代人工智能 ERP 系统
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
  <!-- Contributors -->
  <a href="https://github.com/wansenai/wansenerp/graphs/contributors">
    <img alt="GitHub contributors" src="https://img.shields.io/github/contributors/wansenai/wansenerp">
  </a>
  <!-- bors -->
  <a href="#">
    <img alt="GitHub last commit (branch)" src="https://img.shields.io/github/last-commit/wansenai/wansenerp/master">
  </a>
</div>
<br />

在 ERP 业务的基础上，我们扩展了 GPT-3.5，个人或公司可以通过我们的系统配置您的模型，并通过 Docker 或其他方法进行部署。

您可以通过您的简单描述提供完全自动化的业务表单提交操作，并且可以与 GPT 聊天、互动、咨询信息。

**注意：这是一个前仓库收拾的项目。该仓库仓库代码，仓库仓库请浏览[这里](https://github.com/wansenai/wansenerpui)。**

## 在线预览

我们已将程序部署在Amazon Cloud上，请您在使用前先体验一下。 
如果您有任何疑问或建议，请提交[issue](https://github.com/wansenai/WansenERP/issues/new)，我们将会及时处理。

预览地址：http://erp.wansen.cloud/

## 模块说明

1. **Domain Module:**

业务操作数据对象。
```xml
<dependency>
    <groupId>com.wansensoft</groupId>
    <artifactId>domain</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

2. **Dao Module:**

数据访问对象将数据访问逻辑与业务逻辑分开，以提供对数据持久存储的访问和操作。
```xml
<dependency>
    <groupId>com.wansensoft</groupId>
    <artifactId>dao</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

3. **Service Module:**

业务逻辑层，将具体的业务逻辑处理委托给服务层。
```xml
<dependency>
    <groupId>com.wansensoft</groupId>
    <artifactId>service</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

4. **Utils Module:**

封装了一些具体操作的工具类代码。
```xml
<dependency>
    <groupId>com.wansensoft</groupId>
    <artifactId>utils</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

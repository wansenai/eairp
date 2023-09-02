<h1 align="center">WanSen ERP</h1>
<div align="center">
 <strong>
  Next generation intelligent AI ERP system
 </strong>
</div>

<br />
On the basis of ERP business, we have expanded GPT-3.5. You, individually or as a company, can fine tune your model through our system and deploy it through Docker or other methods. 

You can provide fully automated business form submission operations through your simple description, and you can chat, interact, and consult information with GPT.

## Online preview

We have deployed this program on Amazon Cloud for you to experience before deciding to use it. 
If you have any questions or suggestions, please submit [issue](https://github.com/wansenai/WansenERP/issues/new) and we will handle them in a timely manner.

Experience address: http://140.179.135.174:3000/

## Explanation between modules

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
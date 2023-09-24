---
title: WanSenERP API v1.0.0
language_tabs:
  - shell: Shell
  - http: HTTP
  - javascript: JavaScript
  - ruby: Ruby
  - python: Python
  - php: PHP
  - java: Java
  - go: Go
toc_footers: []
includes: []
search: true
code_clipboard: true
highlight_theme: darkula
headingLevel: 2
generator: "@tarslib/widdershins v4.0.11"

---

# WanSenERP API

> v1.0.0

# Default

## POST 用户注册

POST /users/insert

> Body 请求参数

```json
{
  "user_name": "string",
  "password": "string",
  "email": "string",
  "phone_number": "string",
  "name": "string"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|object| 否 |none|
|» user_name|body|string| 是 |none|
|» password|body|string| 是 |none|
|» email|body|string| 是 |none|
|» phone_number|body|string| 是 |none|
|» name|body|string| 是 |none|

> 返回示例

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

# 数据模型


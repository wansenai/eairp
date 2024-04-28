<p align="center">
<!--   <a href="https://www.antdv.com/">
    <img width="350" src="/images/wansenai-logo.png">
  </a> -->
</p>
<h1 align="center">
  <a href="#" target="_blank">Enterprise AI Resource Planning Web</a>
</h1>
<div align="center">
  <!-- nodejs build status-->
  <a href="https://github.com/wansenai/wansen-erp/blob/master/.github/workflows/node.js.yml">
    <img src="https://img.shields.io/github/actions/workflow/status/wansenai/wansen-erp/node.js.yml"
    alt="Crates.io version" />
  </a>
  <a href="">
    <img src="https://img.shields.io/github/repo-size/wansenai/wansen-erp"/>
  </a>
  <a href="">
    <img src="https://img.shields.io/github/last-commit/wansenai/wansen-erp"/>
  </a>

</div>

<div align="center">
   <strong>下一代人工智能ERP系统</strong>
</div>
<br />

## Project
[英语](README.md) | [简体中文](./README-zh_CN.md)

### 快速开始

您可以直接使用Docker pull镜像来快速启动。下面是拉取前端镜像的命令

### 拉取镜像
```shell
docker pull wansenai/eairp-web:2.1.1
```

### 运行服务
请注意该 `API_BASE_URL`参数，这是后端接口映射的地址。如果你是在你的服务器
上部署，请将此处的localhost地址修改为你的服务器IP地址。
```shell
docker run --name eairp-web -d -p 3000:80 -e API_BASE_URL=http://localhost:8080/erp-api wansenai/eairp-web:2.1.1
```
如果你想使用Docker部署API，也可以拉取API镜像
```shell
docker pull wansenai/eairp:2.1.1
```
并且运行API服务
```shell
docker run --name eairp -d -p 9998:8088 wansenai/eairp:2.1.1
```

### 在线预览
- [eairp 在线预览](https://erp.wansen.cloud/)
- 测试账号: wansen
- 测试密码: 123456
- 部分功能模块正在开发和完善中，请参考我们的[待办事项列表](https://github.com/wansenai/eairp-web/issues/41)。用爱发电并不容易。
- 如果这个项目对您有帮助，请点击Star。谢谢。

  一些功能模块正在开发和改进中, 请参阅我们的[开发计划](https://github.com/wansenai/eairp-web/issues/42), 用爱发电很不容易, 如果这个项目对你有帮助, 请点击Star非常感谢.

### 代码存储库
- [wansen-erp](https://github.com/wansenai/wansen-erp) - **ERP Web 模板**

## 浏览器支持

`Chrome 80+` 本地开发推荐使用浏览器

支持现代浏览器，不支持 IE

| [<img src="https://raw.githubusercontent.com/alrra/browser-logos/master/src/edge/edge_48x48.png" alt=" Edge" width="24px" height="24px" />](http://godban.github.io/browsers-support-badges/)</br>IE | [<img src="https://raw.githubusercontent.com/alrra/browser-logos/master/src/edge/edge_48x48.png" alt=" Edge" width="24px" height="24px" />](http://godban.github.io/browsers-support-badges/)</br>Edge | [<img src="https://raw.githubusercontent.com/alrra/browser-logos/master/src/firefox/firefox_48x48.png" alt="Firefox" width="24px" height="24px" />](http://godban.github.io/browsers-support-badges/)</br>Firefox | [<img src="https://raw.githubusercontent.com/alrra/browser-logos/master/src/chrome/chrome_48x48.png" alt="Chrome" width="24px" height="24px" />](http://godban.github.io/browsers-support-badges/)</br>Chrome | [<img src="https://raw.githubusercontent.com/alrra/browser-logos/master/src/safari/safari_48x48.png" alt="Safari" width="24px" height="24px" />](http://godban.github.io/browsers-support-badges/)</br>Safari |
| :-: | :-: | :-: | :-: | :-: |
| 不支持 | 最新的 2 个版本 | 最新的 2 个版本 | 最新的 2 个版本 | 最新的 2 个版本 |

## 系统截图（仅部分）
![](images/login-page-en.png)
![](images/home-page-zh.png)
![](images/user-manage-zh.png)
![](images/add-menu-zh.png)
![](images/role-permission-zh.png)

## 安装与使用

- 获取项目代码

```bash
git clone https://github.com/wansenai/eairp.git

cd eairp
```

如果您尚未安装pnpm工具，请使用以下命令首先安装pnpm
```bash
npm install -g pnpm
```

- 安装依赖

```bash
pnpm install
```

- 运行服务
```bash
pnpm serve
```

- 构建服务
```bash
pnpm build
```

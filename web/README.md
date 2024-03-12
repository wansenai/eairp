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
   <strong>Next generation artificial intelligent ERP system.</strong>
</div>
<br />

## Project
[English](README.md) | [简体中文](./README-zh_CN.md)

### Quick Start

You can directly use the Docker pull image for quick startup. Here are the commands to pull the front-end image

### Pull images
```shell
docker pull wansenai/eairp-web:2.0.3
```

### Run Server
Please note the `API_BASE_URL` parameter, this is the address mapped by the back-end interface.
If you are deploying on your server, modify the localhost address here to your server IP.
```shell
docker run --name eairp-web -d -p 3000:80 -e API_BASE_URL=http://localhost:8080/erp-api wansenai/eairp-web:2.0.3
```
If you want to deploy the API using Docker, you can also pull the API image
```shell
docker pull wansenai/eairp:2.0.3
```
And run API services
```shell
docker run --name eairp -d -p 9998:8088 wansenai/eairp:2.0.3 
```

### Online preview
- [eairp preview / 在线预览](https://erp.wansen.cloud/)
- test account (测试账号): wansen
- test password (测试密码): 123456
- Some functional modules are being developed and improved, please refer to our [to-do list](https://github.com/wansenai/eairp-web/issues/41). It's not easy to generate electricity with love.
- If this project is helpful to you, please click on Star. Thank you.

  一些功能模块正在开发和改进中, 请参阅我们的[开发计划](https://github.com/wansenai/eairp-web/issues/42), 用爱发电很不容易, 如果这个项目对你有帮助, 请点击Star非常感谢.

### Repository code
- [wansen-erp](https://github.com/wansenai/wansen-erp) - **ERP Web Template**

## Browser support

The `Chrome 80+` browser is recommended for local development

Support modern browsers, not IE

| [<img src="https://raw.githubusercontent.com/alrra/browser-logos/master/src/edge/edge_48x48.png" alt=" Edge" width="24px" height="24px" />](http://godban.github.io/browsers-support-badges/)</br>IE | [<img src="https://raw.githubusercontent.com/alrra/browser-logos/master/src/edge/edge_48x48.png" alt=" Edge" width="24px" height="24px" />](http://godban.github.io/browsers-support-badges/)</br>Edge | [<img src="https://raw.githubusercontent.com/alrra/browser-logos/master/src/firefox/firefox_48x48.png" alt="Firefox" width="24px" height="24px" />](http://godban.github.io/browsers-support-badges/)</br>Firefox | [<img src="https://raw.githubusercontent.com/alrra/browser-logos/master/src/chrome/chrome_48x48.png" alt="Chrome" width="24px" height="24px" />](http://godban.github.io/browsers-support-badges/)</br>Chrome | [<img src="https://raw.githubusercontent.com/alrra/browser-logos/master/src/safari/safari_48x48.png" alt="Safari" width="24px" height="24px" />](http://godban.github.io/browsers-support-badges/)</br>Safari |
| :-: | :-: | :-: | :-: | :-: |
| not support | last 2 versions | last 2 versions | last 2 versions | last 2 versions |

## System screenshot (only part)
![](images/login-page-en.png)
![](images/home-page-zh.png)
![](images/user-manage-zh.png)
![](images/add-menu-zh.png)
![](images/role-permission-zh.png)

## Install and use

- Get the project code

```bash
git clone https://github.com/wansenai/eairp.git

cd eairp
```

If you have not installed the pnpm tool, please use the following command to install pnpm first
```bash
npm install -g pnpm
```

- Installation dependencies

```bash
pnpm install
```

- run
```bash
pnpm serve
```

- build
```bash
pnpm build
```

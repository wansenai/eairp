# Eairp是什么

[Eairp](https://github.com/wansenai/eairp) (Enterprise AI Resource Planning) 是一套面向企业的综合资源规划系统，旨在优化整合企业各项运营流程，提高管理效率，降低运营成本。
EAIRP包含物料采购、财务预算、库存管理、账单管理、用户角色组织管理等多项功能，并引入先进的AI助手，为企业提供智能管理解决方案。

# 目录
- [介绍](#introduction)
- [如何使用 Eairp Image](#how-to-use-this-image)
    -	[拉取现有的镜像](#pulling-an-existing-image)
        -	[使用 docker run](#using-docker-run)
        -	[使用 docker-compose](#using-docker-compose)
    -	[构建](#building)
- [升级 Eairp](#upgrading-eairp)
- [故障排除](#troubleshooting)
- [Eairp Image 详细信息](#details-for-the-eairp-image)
    -	[配置选项](#configuration-options)
    -	[配置文件](#configuration-files)
    -	[其他](#miscellaneous)
- [执照](#license)
- [支持](#support)
- [贡献](#contribute)
- [致谢](#credits)

# Introduction

目标是提供在 Docker 中运行的可用于生产的 Eairp 系统。原因如下：

-	该操作系统基于 Debian，而不是基于一些占用空间较小的发行版，如 Alpine
-	Docker Compose 使用了多个容器：一个用于 mysql，一个用于 Redis，另一个用于 Eairp + Nginx。这样就可以在不同的机器上运行它们。

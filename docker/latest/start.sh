#!/bin/bash

# 替换前端配置
if [ -f /usr/share/nginx/html/dist/assets/index.js ]; then
    echo "替换前端 index.js 配置"
    sed -i "s|api_base_url|${API_BASE_URL}|g" /usr/share/nginx/html/dist/assets/index.js
else
    echo "index.js not found"
fi

if [ -f /usr/share/nginx/html/dist/_app.config.js ]; then
    echo "替换前端 _app.config.js 配置"
    sed -i "s|api_base_url|${API_BASE_URL}|g" /usr/share/nginx/html/dist/_app.config.js
else
    echo "_app.config.js not found"
fi

# 启动 Java服务在后台
echo "启动 Java服务"

# Set the active Spring profile
SPRING_PROFILE=${SPRING_PROFILE:-docker}

java -Dspring.profiles.active=${SPRING_PROFILE} -cp . -jar application.jar &

# 启动 nginx 并保持在前台
echo "启动 Nginx"
nginx -g 'daemon off;' &

# 等待后台进程结束
wait -n

# 退出时关闭所有子进程
trap 'kill $(jobs -p)' EXIT
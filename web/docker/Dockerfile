FROM nginx:stable-alpine3.17-slim

COPY dist/ /usr/share/nginx/html/
COPY ../deploy/default.conf /etc/nginx/conf.d/

LABEL MAINTAINER="team@wansenai.com"

EXPOSE 80

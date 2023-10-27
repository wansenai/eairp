FROM nginx:1.23.4-alpine

COPY dist/ /usr/share/nginx/html/
COPY ../deploy/default.conf /etc/nginx/conf.d/

LABEL MAINTAINER="team@wansenai.com"

EXPOSE 80

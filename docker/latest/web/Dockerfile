FROM node:18.18.0 as build-stage
MAINTAINER WanSen AI<team@wansenai.com>
WORKDIR /app

COPY . ./

# 设置 node 阿里镜像
RUN npm config set registry https://registry.npmmirror.com

ENV NODE_OPTIONS --max-old-space-size=16384

RUN npm install pnpm -g && \
    pnpm install --frozen-lockfile && \
    pnpm build:docker

RUN echo "Build Success"

FROM nginx:1.23.4 as production-stage
COPY --from=build-stage /app/dist /usr/share/nginx/html/dist
COPY --from=build-stage /app/nginx.conf /etc/nginx/nginx.conf
COPY start.sh /usr/share/nginx/html/start.sh
RUN chmod +x /usr/share/nginx/html/start.sh
EXPOSE 80

CMD ["/usr/share/nginx/html/start.sh"]

RUN echo "Run Success"
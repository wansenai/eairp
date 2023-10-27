FROM node:18.18.0 as build-stage
MAINTAINER WanSen AI<team@wansenai.com>
WORKDIR app

COPY ./web ./

ENV NODE_OPTIONS --max-old-space-size=16384

RUN npm install pnpm -g
RUN pnpm install --frozen-lockfile
RUN pnpm build:docker

RUN echo "Build Success"

FROM nginx:1.23.4 as production-stage
COPY --from=build-stage /app/dist /usr/share/nginx/html/dist
COPY --from=build-stage /app/nginx.conf /etc/nginx/nginx.conf
EXPOSE 80

CMD sed -i "s|api_base_url|$API_BASE_URL|g" /usr/share/nginx/html/dist/assets/index.js && \
    sed -i "s|api_base_url|$API_BASE_URL|g" /usr/share/nginx/html/dist/_app.config.js && \
    nginx -g 'daemon off;'

RUN echo "Run Success"

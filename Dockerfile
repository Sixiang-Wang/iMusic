# 使用 Node 镜像构建 Vue 项目
FROM node:14.21.2-alpine as build-stage

# 设置工作目录
WORKDIR /app

# 复制 package.json 和 package-lock.json（如果存在）到工作目录
COPY package.json package-lock.json* ./

# 安装依赖
RUN npm install

# 复制所有文件到工作目录
COPY . .

# 构建 Vue 项目
RUN npm run build

# 使用 Nginx 镜像来服务静态文件
FROM nginx:1.21.0-alpine

# 复制构建后的文件到 Nginx 目录
COPY --from=build-stage /app/dist /usr/share/nginx/html

# 暴露端口
EXPOSE 80

# 启动 Nginx
CMD ["nginx", "-g", "daemon off;"]


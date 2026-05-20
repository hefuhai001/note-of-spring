FROM alpine:3.18

# 使用阿里云镜像源加速
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories

# 一次性安装所有运行时，减少镜像层数
#RUN apk add --no-cache \
#    gcc g++ musl-dev make \
#    openjdk17 \
#    python3 py3-pip \
#    nodejs npm \
#    rust cargo \
#    go

RUN apk add --no-cache \
    gcc g++ musl-dev make \
    openjdk17 \
    python3 py3-pip \
    nodejs npm \
    go

# .NET 单独安装（需要额外配置全局工具）
#RUN apk add --no-cache dotnet7-sdk && \
#    dotnet tool install --global dotnet-script && \
#    ln -s /root/.dotnet/tools/dotnet-script /usr/local/bin/dotnet-script

ENV PATH="${PATH}:/root/.dotnet/tools"

# 确保 nobody 用户可执行
RUN mkdir -p /workspace && chmod 777 /workspace

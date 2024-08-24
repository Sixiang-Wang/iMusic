@echo off
REM 构建 Docker 镜像
docker rmi client
docker build -t client .

REM 获取新构建的镜像 ID
FOR /F "tokens=*" %%i IN ('docker images client --format "{{.ID}}"') DO SET IMAGE_ID=%%i

REM 打标签并推送到阿里云 Docker Registry
docker tag %IMAGE_ID% registry.cn-beijing.aliyuncs.com/imusic/client:1.1
docker push registry.cn-beijing.aliyuncs.com/imusic/client:1.1

kubectl delete deployment client
kubectl delete svc client

REM 应用 Kubernetes 配置
kubectl apply -f .\app.yaml

REM 等待所有 client pods 处于 Running 状态
echo Waiting for client pods to be in Running state...
:wait_loop
REM 获取所有 client pods 的状态
kubectl get pods -l app=client > pods_status.txt

REM 初始化变量
SET "ALL_RUNNING=true"

REM 解析 pod 状态
FOR /F "skip=1 tokens=1,3" %%a IN (pods_status.txt) DO (
    IF NOT "%%b"=="Running" (
        SET "ALL_RUNNING=false"
        echo Pod %%a is in state %%b
    )
)

REM 如果所有 pods 都在 Running 状态，则继续
IF "%ALL_RUNNING%"=="true" (
    echo All client pods are running.
    GOTO continue
)

REM 如果不是，等待 5 秒后再检查状态
timeout /t 3 > nul
GOTO wait_loop
:continue

kubectl apply -f .\service.yaml
kubectl get pod
kubectl get svc

echo ====================
echo Deployment complete
echo     请勿关闭此窗口
echo ====================

kubectl port-forward service/client 2234:2234

pause
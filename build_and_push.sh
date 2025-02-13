V
#!/bin/bash

# 设置变量
APP_NAME="app"  # 你的应用程序名称
ECR_REGION="us-east-1"  # 你的 AWS ECR 区域
ECR_ACCOUNT_ID="536697229825"  # 你的 AWS 账户 ID
ECR_REPO_NAME="ljh"  # 你的 ECR 仓库名称

# 打包应用程序
echo "Packaging the application..."
mvn clean package || { echo 'Maven build failed' ; exit 1; }

# 构建 Docker 镜像
echo "Building Docker image..."
docker build -t $APP_NAME:latest . || { echo 'Docker build failed' ; exit 1; }

# 登录 ECR
echo "Logging in to Amazon ECR..."
aws ecr get-login-password --region $ECR_REGION | docker login --username AWS --password-stdin $ECR_ACCOUNT_ID.dkr.ecr.$ECR_REGION.amazonaws.com || { echo 'ECR login failed' ; exit 1; }

# 标记 Docker 镜像
echo "Tagging Docker image for ECR..."
docker tag $APP_NAME:latest $ECR_ACCOUNT_ID.dkr.ecr.$ECR_REGION.amazonaws.com/$ECR_REPO_NAME:latest || { echo 'Docker tag failed' ; exit 1; }

# 推送到 ECR
echo "Pushing Docker image to Amazon ECR..."
docker push $ECR_ACCOUNT_ID.dkr.ecr.$ECR_REGION.amazonaws.com/$ECR_REPO_NAME:latest || { echo 'Docker push to ECR failed' ; exit 1; }

echo "Docker image has been successfully pushed to Amazon ECR."
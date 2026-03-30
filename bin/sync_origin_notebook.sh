#!/bin/bash
 
# 配置这些变量
# REPO_PATH=~/workspaces/workspace_learning/The-Notebook # 你的Git仓库路径
COMMIT_MESSAGE=$(date +"%Y-%m-%d %H:%M:%S") # 提交信息为当前时间戳

# 进入仓库目录
#cd $REPO_PATH
cd ../

# 检查是否有未提交的更改
if [ -n "$(git status --porcelain)" ]; then
  # 有未提交的更改，先暂存
  git add .
  # 提交更改
  git commit -m "[AUTO-COMMIT] $COMMIT_MESSAGE"
  # 推送到远程仓库
  git push origin master
else
  echo "No changes to commit."
fi

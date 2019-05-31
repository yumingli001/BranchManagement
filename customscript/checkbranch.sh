#!/bin/bash
if [ $# -eq 4 ];then
 echo "开始执行git脚本...";
 echo "项目名：$1 , git克隆地址：$2 ， 你的新建分支名称：$3  , 需要发布的环境：$4";
else
 echo "脚本执行需要4个参数：项目名 git克隆地址 你的新建分支名称 需要发布的环境"
 exit -1
fi
#获取当前执行脚本路径

dir=`pwd`

#获取今天的日期,格式:yyyymmddhhmmssnn

time=`date +%Y%m%d%H%M%S%N | cut -b 1-16`

#项目名

project=$1

#git clone 地址

gitcloneurl=$2

#你的本地分支名称

feature=$3

#你的环境变量
#识别dev、beta、gray、prod

environment=$4

#检测分支是否落后于master
function checkbackwardmaster(){
 if [ $(git log $1..origin/master|wc -l) -ne 0 ]; then
  echo "没有合并master，请联系开发合并主干分支"
  exit -1
 else
  return 0
 fi 
}
#检测是否是btag
function checkbtag(){
 if [[ "$1" =~ ^b-[1-9][0-9]{15}-\g$ ]];then
  return 0
 else
  return 1
 fi
}
#检测是否是rtag
function checkrtag(){
 if [[ "$1" =~ ^r-[1-9][0-9]{15}-\g$ ]];then
  return 0
 else
  return 1
 fi
}


#远端拉取
if [ ! -x "$dir/$project/.git/" ];then
 git clone  $gitcloneurl $dir/
 if [ $? -ne 0 ]; then
  echo "git clone url 错误"
  exit -1
 fi
else 
 git fetch --all
fi
#检测是否存在当前分支
if [ $(git branch -a |grep -w remotes/origin/$feature|wc -l) == 0 ] && [ $(git tag | grep -x $feature|wc -l) == 0 ];then
 echo "无当前分支请重新确认分支"
 exit -1
fi

#切换分支
git checkout master
git reset --hard origin/master
git branch | grep -v -E 'master' | xargs git branch -D
if checkrtag $feature;then
 git checkout -b $feature $feature
elif checkbtag $feature;then
 git checkout -b $feature $feature
else
 git checkout -b $feature origin/$feature
fi

#判断所在环境
if [ "$environment" != "prod" ];then
#非线上环境判断是否是btag或rtag
 if checkrtag $feature;then
  echo "开始编译"
 elif checkbtag $feature;then
  echo "开始编译"
 else
  #分支检测是否合并master并打btag
  checkbackwardmaster $feature
  btag=b-$time-\g
  git tag $btag
  git push origin $btag
 fi
else
#线上环境判断只能用tag发布
 if checkrtag $feature;then
  echo "开始编译"
 elif checkbtag $feature;then
  #检测是否需要合并分支并打rtag
  checkbackwardmaster $feature
  rtag=r-$time-\g
  git tag $rtag
  git push origin $rtag
  git checkout master
  git merge $rtag
  git push
 else
  echo "请使用b-tag发布，或r-tag回滚"
  exit -1
 fi
fi











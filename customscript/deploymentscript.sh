#!/bin/bash
if [ $# -eq 5 ];then
echo "开始执行上传脚本...";
echo "发布机器ip：$1 , tomcat启动端口：$4 , 项目名称：$5";
else
echo "脚本执行需要5个参数：发布机器ip tag包目录 发布机器目录 tomcat启动端口 项目名称";
exit -1;
fi
#获取当前执行脚本路径

dir=`pwd`

#发布机器ip

ip=$1

#账号，发布机器登陆账号

account="ubuntu"

#tag包目录

packagePath=${2%*/}

#发布目录

deployPath=${3%*/}

#tomcat启动端口

port=$4

#项目名

project=$5

#目标机器

target_machine="ssh $account@$ip"

#判断是jar包war包并更改包名

cd $dir/$packagePath
if [ $(ls |grep -x *.jar|wc -l) == 1 ];then
 mv $(ls |grep -x *.jar) $project.jar
 envm=jar
elif [ $(ls |grep -x *.war|wc -l) == 1 ];then
 mv $(ls |grep -x *.war) $project.war
 envm=war
else
 echo "没有找到准确的jar包或war包";
 exit -1;
fi

#建立发布目录

if $target_machine test ! -x "~/$deployPath";then
 ssh ubuntu@$ip "mkdir -p ~/$deployPath"
fi

echo "上传deploy包"
#传jar包
if [ $envm == "jar" ];then 
 scp $dir/$packagePath/$project.jar $account@$ip:~/$deployPath/deploy.jar
 if  $target_machine test ! -e "~/$deployPath/jar-service.sh";then
  scp $dir/../../customscript/jar-service.sh $account@$ip:~/$deployPath/
 fi
else
#传war包
 echo "11111111111111111111111111111111111111111"
fi

#启动jar包
if [ $envm == "jar" ];then
 $target_machine "cd $deployPath
 ./jar-service.sh $project.jar stop
 mv deploy.jar $project.jar
 ./jar-service.sh $project.jar start $port
 "
else
 echo "11111111111111111111111111111111111111111"
fi






















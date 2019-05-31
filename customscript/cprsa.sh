#!/bin/bash
if [ $# -eq 3 ];then
echo "开始执行设置免密登陆脚本...";
echo "免密登陆ip：$1 , 账号：$2 ， 密码：$3";
else
echo "脚本执行需要3个参数：免密登陆ip 账号 密码";
exit -1;
fi
#获取当前执行脚本路径

dir=`pwd`

#免密登陆ip

ip=$1

#账号

account=$2

#密码

passwd=$3

#本机公钥

publickey=$(cat $dir/../../.ssh/id_rsa.pub)

#目标机器

target_machine="sshpass -p $3 ssh $2@$1"

#追加本机公钥到目标机上
$target_machine "if [ ! -f "~/.ssh/authorized_keys" ];then 
touch "~/.ssh/authorized_keys";
fi"
$target_machine "chmod 600 "~/.ssh/authorized_keys""
$target_machine "if grep "\'$publickey\'" "~/.ssh/authorized_keys";then 
echo "$ip 已设置免密登陆";
else
echo "$publickey" >> "~/.ssh/authorized_keys";
fi"

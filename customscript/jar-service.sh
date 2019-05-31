#!/bin/bash
#使用说明，用来提示输入参数
usage() {
    echo "脚本执行需要3个参数： jar包 启动方式 启动端口(只需启动填写)";
    echo "Usage: sh jar-service.sh test.jar [start|stop|restart|status] 8999";
    exit -1;
}


if [ $# -eq 3 ] || [ $# -eq 2 ];then
echo "开始执行脚本...";
else
usage
fi
appname=$1
appstatus=$2
apppost=$3
#检查程序是否在运行
is_exist() {
    pid=`ps -ef | grep $appname|grep "java -jar" | awk '{print $2}'`
    #如果不存在返回0，存在返回1
    if [ ! -z "$pid" ]; then
      apppost=`sudo netstat -antp | grep $pid | awk '{print $4}'|cut -f2 -d":"`
      return 1
    else
      return 0
    fi
}
is_success(){
    for ((i=1;i<=60;i++));do
     sleep 1s
     nc -v -z localhost $apppost
     if [ $? -eq "0" ]; then
        echo "$appname 启动成功"
        exit
     fi
     echo "检查服务是否启动成功"
     done
     echo "启动超时"
     exit -1
}
is_portexist(){
    porte=`sudo netstat -anp|grep -w $apppost|wc -l`
    if [ $porte != 0 ]; then
      echo "当前机器端口被占用，请联系运维人员"
      exit -1
    fi
}
#启动方法
start() {
   echo "开始启动app"
   is_exist
   if [ $? -eq "1" ]; then
     echo "$appname 正在运行，pid：$pid , 端口：$apppost."
   else
     if [ -z "$apppost" ]; then
      echo "启动脚本请指定端口号"
      exit -1
     fi
     is_portexist
     if [ ! -x "./logs/" ];then
       mkdir ./logs
     fi
     if [ -f "./logs/startup.log" ];then
     mv ./logs/startup.log ./logs/startup.log.bak
     fi
     nohup java -jar -Xms258m -Xmx258m -XX:PermSize=512M -XX:MaxPermSize=512m  ./$appname > ./logs/startup.log 2>&1 --server.port=$apppost &
     is_success
   fi
}
#停止方法
stop() {
   echo "开始停止app"
   is_exist
   if [ $? -eq "1" ]; then
     kill $pid
     for ((i=1;i<=60;i++));do
     sleep 1s
     is_exist
     echo "检查服务是否还存在"
     if [ $? -eq "0" ]; then
        for ((i=1;i<=90;i++));do
         sleep 1s
         echo "检查服务是否还存在"
         porte=`sudo netstat -anp|grep -w $apppost|wc -l`
         if [ $porte == 0 ]; then 
            echo "停止成功app"
            return 1
         fi
        done
        echo "停止超时"
        return 0
     fi
     done
     echo "停止超时"
     return 0
   else
     echo "$appname 已经停止"
   fi
}
#输出运行状态
status() {
   is_exist
   if [ $? -eq "1" ]; then
     echo "$appname 正在运行，pid：$pid , 端口：$apppost"
   else
     echo "$appname 已经停止"
   fi
}
#重启
restart() {
   stop
   if [ $? -eq "1" ]; then
    start
   fi
   echo "停止失败"
}
#根据输入参数，选择执行对应方法，不输入则执行使用说明
case "$2" in
   "start")
     start
     ;;
   "stop")
     stop
     ;;
   "status")
     status
     ;;
   "restart")
     restart
     ;;
   *)
     usage
     ;;
esac




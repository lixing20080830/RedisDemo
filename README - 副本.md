# RedisDemo
spring3.1 redis cache
needed jedis lib

遗留问题
1.实现高并发下的计数器<br>
2.id自增<br>
3.jedis 对二进制进行操作<br>

jedis客户端版本过高，我采用的是2.7.2的版本的jedis<br>
spring-data-redis用的1.4.2的版本,jedis的版本号换位2.6.2以下就好了<br>


centos7安装redis3.2 安装步骤说明：<br>
1.下载好redis3.2.3.tar文件<br>
2.tar zxvf redis3.2.3.tar 到/usr/local/下<br>
3.make(必须安装好gcc--gcc++)<br>
4.make install(make clean)<br>
5.修改redis.conf配置文件(注释：#bind 127.0.0.1)<br>
6.修改redis.conf配置文件(protected-mode no,连接时不需要密码)<br>
7.开启端口：开启端口<br>
firewall-cmd --zone=public --add-port=6379/tcp --permanent<br>
8.重启防火墙<br>
firewall-cmd --reload<br>

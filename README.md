# 远程链接终端
   ### 架构
   ##### 后端
    springboot + mybatis + websocket + h2
   ##### 前端
    vue + element-ui + terminal
    
   ##### 快速运行
    docker run -itd --name y-shell -e active="dev" -p 8082:8082 -v /root/.m2:/root/.m2 -v /root/.ssh:/root/.ssh zhangyule1993/y-shell:v1.0.0.release
    
   ###示例图片
   ##### 文件夹管理
   ![1.png](./doc/folder.png)
   
   ##### 连接管理
   ![1.png](./doc/ssh.png)
    

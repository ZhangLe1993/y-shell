# 远程链接终端
   ### 架构
   ##### 后端
    springboot + mybatis + websocket + h2
   ##### 前端
    vue + element-ui + terminal
    
   ##### 快速运行
    docker run -itd --name y-shell -e active="dev" -p 8082:8082 -v /root/.m2:/root/.m2 -v /root/.ssh:/root/.ssh zhangyule1993/y-shell:v1.0.0.release
    
   ### 示例图片
   ##### 文件夹管理
   ![1.png](./doc/folder.png)
   
   ##### 连接管理
   ![1.png](./doc/ssh.png)
   
   #### 本地运行
   ###### 下载代码
    git clone https://github.com/ZhangLe1993/y-shell.git
    
   ###### 进入前端目录
    cd y-shell
    cd y-shell-web
   ###### 安装依赖,有些可能需要手动依赖.在main.js中有注释.
    cnpm install
   ###### 编译打包
    cnpm run build

   ###### 运行springboot
    执行springboot 的 Application方法
    或者直接执行jar包
    cd y-shell/out
    java -Dfile.encoding=utf-8 -jar app.jar --spring.profiles.active=dev
   
    
    
    

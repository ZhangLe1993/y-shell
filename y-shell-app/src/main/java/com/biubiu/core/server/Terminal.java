package com.biubiu.core.server;


import com.biubiu.core.common.Const;
import com.biubiu.model.Remote;
import com.biubiu.utils.SpringContextUtil;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;

@Scope("prototype")
@Component
@Slf4j
public class Terminal {

    @Resource(name = "websocketThreadPool")
    private ExecutorService service;

    private Remote remote;
    private JSch jsch;

    private InputStream inputStream;
    private OutputStream outputStream;

    public Terminal init(Remote remote) {
        if(service == null) {
            service = (ExecutorService) SpringContextUtil.applicationContext.getBean("websocketThreadPool");
        }
        this.remote = remote;
        this.jsch = new JSch();
        return this;
    }

    public void openConnect(javax.websocket.Session wsSession) throws Exception {
        if (Const.AUTH_METHOD.equalsIgnoreCase(remote.getAuthMethod()) && Files.exists(Paths.get(remote.getIdentity()))) {
            jsch.addIdentity(remote.getIdentity(), remote.getPassphrase());
        }
        log.info("user: {},host: {},port: {}, password: {}, identity: {}", remote.getUser(), remote.getHost(), remote.getPort(), remote.getPassword(), remote.getIdentity());
        Session session = jsch.getSession(remote.getUser(), remote.getHost(), remote.getPort());
        session.setPassword(remote.getPassword());
        session.setConfig("StrictHostKeyChecking", "no");
        // 跳过Kerberos身份验证
        session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
        session.connect(3000);
        Channel channel = session.openChannel("shell");
        this.inputStream = channel.getInputStream();
        this.outputStream = channel.getOutputStream();
        channel.connect(3000);
        this.getResp(wsSession);
    }

    private void getResp(javax.websocket.Session wsSession) {
        service.execute(() -> {
            try {
                start(wsSession);
            } catch (IOException e) {
                log.error("", e);
            }
        });
    }

    private void start(javax.websocket.Session wsSession) throws IOException {
        for(;;) {
            // 新建缓冲区
            byte[] buff = new byte[10240];
            // 阻塞接收,前端不需要输入显示太多，全部由后台接收就行
            int readLen = inputStream.read(buff);
            if (readLen != -1) {
                ByteBuffer byteBuffer = ByteBuffer.wrap(buff, 0, readLen);
                byte[] readData = new byte[readLen];
                byteBuffer.position(0);
                byteBuffer.get(readData);
                String data = new String(readData, StandardCharsets.UTF_8);
                log.info("resp data: {}", data);
                this.send(wsSession, data);
            }
        }
    }

    private void send(javax.websocket.Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void exec(String command) {
        try {
            // \r 立即执行,若没检测到\r 即是回车键命令，shell不会执行任何命令。知道检测到\r为止，相当于在shell上输入了一串命令并没有按回车执行
            byte[] bytes = command.getBytes(StandardCharsets.UTF_8);
            outputStream.write(bytes);
            outputStream.flush();
            log.info(command);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}


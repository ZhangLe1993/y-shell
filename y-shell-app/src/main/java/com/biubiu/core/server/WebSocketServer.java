package com.biubiu.core.server;

import com.alibaba.fastjson.JSON;
import com.biubiu.core.common.Const;
import com.biubiu.dao.EcsDao;
import com.biubiu.model.MessageModel;
import com.biubiu.model.Remote;
import com.biubiu.pojo.Ecs;
import com.biubiu.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@Slf4j
@ServerEndpoint("/terminal")
@Component
public class WebSocketServer {

    @Resource
    private Terminal terminal;

    @Resource
    private EcsDao ecsDao;

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        if(ecsDao == null) {
            ecsDao = SpringContextUtil.getBean(EcsDao.class);
        }
        log.info("websocket连接 sessionId = {}", session.getId());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {

    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) throws Exception {
        log.info("message: {}", message);
        MessageModel messageModel = JSON.parseObject(message, MessageModel.class);
        switch(messageModel.getType()) {
            case "connect":
                String nodeId = messageModel.getData();
                Ecs ecs = ecsDao.findById(Long.parseLong(nodeId));
                Remote remote = JSON.parseObject(ecs.getConfig(), Remote.class);
                if(StringUtils.isNotBlank(remote.getIdentity())) {
                    remote.setIdentity(Const.upload + remote.getHost() + "/" + remote.getIdentity());
                }
                if(null == terminal) {
                    terminal = new Terminal();
                    terminal = terminal.init(remote);
                    terminal.openConnect(session);
                }
                break;
            case "command":
                log.info("command: {}", messageModel.getData());
                String cmd = messageModel.getData();
                this.terminal.exec(cmd);
            default:
                break;
        }
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("", error);
    }

}

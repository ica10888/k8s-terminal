package com.kube.controller.ws;


import java.util.concurrent.*;

import com.kube.dto.WsConnection;
import com.kube.util.URIQueryUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


@Component
public class SpringWebSocketHandler extends TextWebSocketHandler {


    private static final int corePoolSize = 10;
    private static final int maximumPoolSize = 100;
    private static final ConcurrentHashMap<WebSocketSession,WsConnection> wsConnectionMap= new ConcurrentHashMap<>();
    private static final ExecutorService threadPoolExecutor = new ThreadPoolExecutor(
                    corePoolSize,
                    maximumPoolSize,
                    5000,
                    TimeUnit.MILLISECONDS,
                    new ArrayBlockingQueue<>(5));

    private static Logger logger = Logger.getLogger(SpringWebSocketHandler.class);




    public SpringWebSocketHandler() {
    }

    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        if (wsConnectionMap.size() < maximumPoolSize ) {
            if(! wsConnectionMap.containsKey(session) ){
                WsConnection wsConnection = new WsConnection(URIQueryUtil.splitQuery(session.getUri()), session);
                wsConnectionMap.put(session, wsConnection);
                threadPoolExecutor.submit(wsConnection);
                logger.info("Exec connected... count:" + wsConnectionMap.size());
            }
        } else {
            logger.warn("thread pool is full");
            TextMessage textMessage = new TextMessage("thread pool is full".getBytes());
            session.sendMessage(textMessage);
        }


    }

    /**
     * 关闭连接时触发
     */
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        if(wsConnectionMap.containsKey(session)){
            wsConnectionMap.get(session).exit();
            wsConnectionMap.remove(session);
            logger.info("Exec disconnected... count:" + wsConnectionMap.size());
        }

    }

    /**
     * js调用websocket.send时候，会调用该方法
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        if (wsConnectionMap.containsKey(session)) {
            wsConnectionMap.get(session).getOutputStream().write(message.getPayload().getBytes());
        }
        super.handleTextMessage(session, message);


    }

    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if(session.isOpen()){
            session.close();
        }
        if(wsConnectionMap.containsKey(session)){
            wsConnectionMap.get(session).exit();
            wsConnectionMap.remove(session);
            logger.info("Exec disconnected because of error ... count:" + wsConnectionMap.size());
        }
    }

    public boolean supportsPartialMessages() {
        return false;
    }





}

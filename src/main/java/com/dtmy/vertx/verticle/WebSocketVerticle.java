package com.dtmy.vertx.verticle;

import com.dtmy.vertx.common.Topic;
import com.dtmy.vertx.pojo.User;
import com.dtmy.vertx.redis.CommonRedis;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <ul>
 * <li>文件包名 : com.dtmy.vertx.verticle</li>
 * <li>创建时间 : 2017/11/24 17:01</li>
 * <li>修改记录 : 无</li>
 * </ul>
 * 类说明：
 *
 * @author zhonghao
 * @version 2.0.0
 */
public class WebSocketVerticle extends AbstractVerticle {
    private static Map<String, ServerWebSocket> connectionMap = new HashMap<>(16);

    private EventBus eventBus;

    private MessageConsumer<Object> consumer;

    private CommonRedis redisClient;

    private User user = new User();

    @Override
    public void start() throws Exception {
        eventBus = vertx.eventBus();
        redisClient = new CommonRedis(vertx);
        consumer = eventBus.consumer(Topic.USERNAME);

        HttpServer server = vertx.createHttpServer();
        websocketMethod(server);
        server.listen(8880);
    }

    public void websocketMethod(HttpServer server) {
        consumer.handler(message -> user.setUsername(message.body().toString()));

        server.websocketHandler(webSocket -> {
            String username = user.getUsername();
            String uuid = webSocket.binaryHandlerID();
            redisClient.set(uuid, username);
            if (!"".equals(username) && !checkID(username)) {
                connectionMap.put(username, webSocket);
            }
            //　WebSocket 连接
            writeTextMessage(webSocket, uuid);
            closeWebSocket(webSocket, username);
        });
    }

    public void writeTextMessage(ServerWebSocket webSocket, String uuid) {
        webSocket.frameHandler(handler -> {
            User user = redisClient.get(uuid, this.user);
            String textData = buildMessage(handler.textData(), user);
            for (Map.Entry<String, ServerWebSocket> entry : connectionMap.entrySet()) {
                entry.getValue().writeFinalTextFrame(textData);
            }
        });
    }

    public void closeWebSocket(ServerWebSocket webSocket, String id) {
        webSocket.closeHandler(handler -> {
            ServerWebSocket remove = connectionMap.remove(id);
            if (!Objects.isNull(remove)) {
                redisClient.del(id);
            }

        });
    }

    public boolean checkID(String id) {
        return connectionMap.containsKey(id);
    }

    public String buildMessage(String srcMessage, User u) {
        String id = u.getUsername();
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        StringBuffer message = new StringBuffer(id);
        message.append(" ");
        message.append(date);
        message.append(" ");
        message.append(time);
        message.append("\r\n");
        message.append(srcMessage);
        return message.toString();
    }
}

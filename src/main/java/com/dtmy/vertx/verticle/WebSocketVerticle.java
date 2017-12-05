package com.dtmy.vertx.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.ext.web.Router;

import java.util.HashMap;
import java.util.Map;

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
    private Map<String, ServerWebSocket> connectionMap = new HashMap<>(16);

    @Override
    public void start() throws Exception {

        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);

        router.route("/").handler(routingContext -> {
            routingContext.response().sendFile("html/ws.html");
        });
        websocketMethod(server);
        server.requestHandler(router::accept).listen(8080);
    }

    public void websocketMethod(HttpServer server) {
        server.websocketHandler(webSocket -> {
            // 获取每一次链接的ID
            String id = webSocket.binaryHandlerID();
            if (!checkID(id)) {
                connectionMap.put(id, webSocket);
            }

            //　WebSocket 连接
            webSocket.frameHandler(handler -> {
                String textData = handler.textData();
                String currID = webSocket.binaryHandlerID();
                for (Map.Entry<String, ServerWebSocket> entry : connectionMap.entrySet()) {
                    if (currID.equals(entry.getKey())) {
                        continue;
                    }
                    entry.getValue().writeTextMessage(textData);
                }
            });

            // WebSocket 关闭
            webSocket.closeHandler(handler -> {
                System.out.println(id + " 关闭连接");
                connectionMap.remove(id);
            });
        });
    }

    public boolean checkID(String id) {
        return connectionMap.containsKey(id);
    }
}

package com.dtmy.vertx.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;

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
    @Override
    public void start() throws Exception {

        Map<String, ServerWebSocket> connectionMap = new HashMap<>(16);

        HttpServer server = vertx.createHttpServer().requestHandler(req -> {
            if ("/".equals(req.uri())) {
                req.response().sendFile("html/ws.html");
            }
        });

        server.websocketHandler(webSocket -> {
            webSocket.frameHandler(handler -> {
                String textData = handler.textData();

                System.out.println(textData);
                webSocket.writeTextMessage(textData);

            });
        }).listen(8080);
    }
}

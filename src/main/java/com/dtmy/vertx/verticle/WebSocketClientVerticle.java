package com.dtmy.vertx.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;

/**
 * <ul>
 * <li>文件包名 : com.dtmy.vertx.verticle</li>
 * <li>创建时间 : 2017/12/4 14:31</li>
 * <li>修改记录 : 无</li>
 * </ul>
 * 类说明：
 *
 * @author zhonghao
 * @version 2.0.0
 */
public class WebSocketClientVerticle extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        HttpClient client = vertx.createHttpClient();

        client.websocket(8080, "localhost", "/myapp", webSocket -> {
            System.out.println("连接上了？");
            webSocket.handler(data -> {
                System.out.println(data.toString("UTF-8"));
                webSocket.writeBinaryMessage(Buffer.buffer(data.toString()));
            });
        });
    }
}

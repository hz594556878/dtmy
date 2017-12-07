package com.dtmy.vertx.verticle;

import com.dtmy.vertx.common.Topic;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.MultiMap;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

/**
 * <ul>
 * <li>文件包名 : com.dtmy.vertx.verticle</li>
 * <li>创建时间 : 2017/12/7 15:03</li>
 * <li>修改记录 : 无</li>
 * </ul>
 * 类说明：
 *
 * @author zhonghao
 * @version 2.0.0
 */
public class PageVerticle extends AbstractVerticle {
    private EventBus eventBus;

    @Override
    public void start() throws Exception {
        HttpServer httpServer = vertx.createHttpServer();
        eventBus = vertx.eventBus();
        Router router = Router.router(vertx);
        router.route("/").handler(content -> content.response().sendFile("html/index.html"));
        router.route("/ws").handler(content -> {
            MultiMap params = content.queryParams();
            String username = params.get("username");
            eventBus.send(Topic.USERNAME, username);
            content.response().sendFile("html/ws.html");
        });
        httpServer.requestHandler(router::accept).listen(8080);
    }
}

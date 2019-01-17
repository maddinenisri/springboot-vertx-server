package com.mdstech.server.verticle;

import com.mdstech.server.config.AppConfiguration;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MesssageProducer extends AbstractVerticle {

    @Autowired
    AppConfiguration configuration;

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);
        // Serve the static pages
//        router.route().handler(StaticHandler.create());

        vertx.createHttpServer()
                .requestHandler(httpRequest -> handleHttpRequest(httpRequest) )
                .listen(configuration.httpPort());
    }

    private void handleHttpRequest(final HttpServerRequest httpRequest) {

        EventBus eventBus = vertx.eventBus();
        eventBus.consumer("state.changed", new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message) {

                //message.body.putString("clientMessage","Hello client");

                eventBus.publish("state.changed","Hello Client");

				/*logger.info("Handler 2 (Shared) received: "

						+ message.body.toString());*/

            }



        });
    }
}

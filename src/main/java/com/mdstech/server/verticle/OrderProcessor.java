package com.mdstech.server.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.springframework.stereotype.Component;

@Component
public class OrderProcessor extends AbstractVerticle {

    private DynamicRouter<JsonObject> router;

    @Override
    public void start() throws Exception {
        router = new DynamicRouter<>(vertx, "topic.orders");
        router.add(OrderProcessor.class.getSimpleName(), this::handle);
    }

    public void handle(final Message<JsonObject> msg) {
        // Handle the order message
        System.out.println("OrderProcessor: " + msg.body().toString() );

    }

    @Override
    public void stop() throws Exception {
        router.remove(OrderProcessor.class.getSimpleName());
    }
}
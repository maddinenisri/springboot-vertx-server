package com.mdstech.server.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.springframework.stereotype.Component;

@Component
public class OrderPersistor extends AbstractVerticle {

    private DynamicRouter<JsonObject> router;

    @Override
    public void start() throws Exception {
        router = new DynamicRouter<>(vertx, "topic.orders");
        router.add(OrderPersistor.class.getSimpleName(), this::handle);
    }

    public void handle(final Message<JsonObject> msg) {
        // Handle the order message
        System.out.println("OrderPersistor: " + msg.body().toString() );
    }

    @Override
    public void stop() throws Exception {
        router.remove(OrderPersistor.class.getSimpleName());
    }
}
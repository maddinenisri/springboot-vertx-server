package com.mdstech.server.verticle;

import java.util.UUID;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import org.springframework.stereotype.Component;

@Component
public class OrderProducer extends AbstractVerticle {

    private DynamicRouter<JsonObject> router;

    @Override
    public void start() throws Exception {
        router = new DynamicRouter<>(vertx, "topic.orders");
        vertx.setPeriodic(1_000, t -> router.route(createOrder()));
    }

    protected JsonObject createOrder() {
        return new JsonObject().put("id", UUID.randomUUID().toString()).put("item", "1");
    }
}
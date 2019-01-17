package com.mdstech.server.verticle;

import io.vertx.amqpbridge.AmqpBridge;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import org.springframework.stereotype.Component;

@Component
public class AMQPMessageConsumer extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        AmqpBridge bridge = AmqpBridge.create(vertx);
// Start the bridge, then use the event loop thread to process things thereafter.
        bridge.start("localhost", 5672, res -> {
            // Set up a consumer using the bridge, register a handler for it.
            MessageConsumer<JsonObject> consumer = bridge.createConsumer("myAmqpAddress");
            consumer.handler(vertxMsg -> {
                JsonObject amqpMsgPayload = vertxMsg.body();
                Object amqpBody = amqpMsgPayload.getValue("body");

                System.out.println("Received a message with body: " + amqpBody);
            });
        });
    }

}

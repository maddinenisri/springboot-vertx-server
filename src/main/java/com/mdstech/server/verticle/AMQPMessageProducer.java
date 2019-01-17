package com.mdstech.server.verticle;

import io.vertx.amqpbridge.AmqpBridge;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.MessageProducer;
import io.vertx.core.json.JsonObject;
import org.springframework.stereotype.Component;

@Component
public class AMQPMessageProducer extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        AmqpBridge bridge = AmqpBridge.create(vertx);
// Start the bridge, then use the event loop thread to process things thereafter.
        bridge.start("localhost", 5672, res -> {
            // Set up a producer using the bridge, send a message with it.
            MessageProducer<JsonObject> producer = bridge.createProducer("myAmqpAddress");

            JsonObject amqpMsgPayload = new JsonObject();
            amqpMsgPayload.put("body", "myStringContent");

            producer.send(amqpMsgPayload);
        });
    }
}

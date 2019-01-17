package com.mdstech.server.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.bridge.BridgeOptions;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.eventbus.bridge.tcp.TcpEventBusBridge;
import org.springframework.stereotype.Component;

@Component
public class TCPBridgeServer extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        System.out.println("Before starting....");
        TcpEventBusBridge tcpEventBusBridge =
                TcpEventBusBridge.create(vertx,
                        new BridgeOptions()
                                .addInboundPermitted(new PermittedOptions().setAddress("welcome"))
                                .addOutboundPermitted(new PermittedOptions().setAddress("welcome")));

        tcpEventBusBridge.listen(10450, res -> {
            if(res.succeeded()) {
                System.out.println("Bus started");
            }
            else {
                System.out.println("Bus failed to start");
            }
        });

        EventBus  eventBus = vertx.eventBus();

        MessageConsumer<JsonObject> consumer = eventBus.consumer("welcome", message -> {
          System.out.println("Message body: "+ message.body());
          System.out.println("Message header: " + message.headers());
          message.reply(new JsonObject("{\"msg\": \"welcome\"}"));
        });
    }
}

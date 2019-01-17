package com.mdstech.server;

import com.mdstech.server.verticle.*;
import io.vertx.core.Vertx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class Application {

    final Vertx vertx = Vertx.vertx();

    @Autowired
    private MesssageProducer messsageProducer;

    @Autowired
    private OrderPersistor orderPersistor;

    @Autowired
    private OrderProcessor orderProcessor;

    @Autowired
    private OrderProducer orderProducer;

//    @Autowired
//    private AMQPMessageConsumer amqpMessageConsumer;

//    @Autowired
//    private AMQPMessageProducer amqpMessageProducer;

    @Autowired
    private TCPBridgeServer tcpBridgeServer;

    public static void main(String []args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void deployVerticle() {
        vertx.deployVerticle(tcpBridgeServer);
//        vertx.deployVerticle(amqpMessageProducer);
    }
}

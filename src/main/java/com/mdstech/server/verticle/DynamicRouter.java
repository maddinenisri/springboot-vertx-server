package com.mdstech.server.verticle;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.shareddata.AsyncMap;

public class DynamicRouter<T> {

    private static final Logger LOG = LoggerFactory.getLogger(DynamicRouter.class);

    private final Vertx vertx;
    private final String address;
    private final String member;
    private AsyncMap<String, String> table;

    public DynamicRouter(Vertx vertx, String address) {
        this(vertx, address, UUID.randomUUID().toString());
    }

    public DynamicRouter(Vertx vertx, String address, String member) {
        this.vertx = Objects.requireNonNull(vertx);
        this.address = Objects.requireNonNull(address);
        this.member = Objects.requireNonNull(member);
    }

    public String getAddress() {
        return address;
    }

    public String getMember() {
        return member;
    }

    public void add(String route, Handler<Message<T>> handler) {
        Objects.requireNonNull(route);
        Objects.requireNonNull(handler);
        put(route, addressRoute -> vertx.eventBus().consumer(addressRoute, handler));
    }

    protected void put(String route, Handler<String> handler) {
        load(map -> {
            String addressMember = createAddressMember(address, route, member);
            String addressRoute = createAddressRoute(address, route);
            map.putIfAbsent(addressMember, addressRoute, ar -> {
                if (ar.succeeded()) {
                    LOG.debug("Added - address: {}, route: {}", address, route);
                    handler.handle(addressRoute);
                } else {
                    LOG.error("Failed to add - address: {}, route: {}", address, route, ar.cause());
                }
            });
        });
    }

    public void remove(String route) {
        Objects.requireNonNull(route);
        load(map -> {
            String addressMember = createAddressMember(address, route, member);
            String addressRoute = createAddressRoute(address, route);
            map.removeIfPresent(addressMember, addressRoute, ar -> {
                if (ar.succeeded()) {
                    LOG.debug("Removed - address: {}, route: {}", address, route);
                } else {
                    LOG.error("Failed to remove - address: {}, route: {}", address, route, ar.cause());
                }
            });
        });
    }

    public void route(T message) {
        Objects.requireNonNull(message);
        load(map -> map.values(ar -> {
            if (ar.succeeded()) {
                LOG.debug("Routing - address: {}, message: {}", address, message);
                Set<String> routes = new HashSet<>(ar.result());
                if (!routes.isEmpty()) {
                    routes.forEach(route -> vertx.eventBus().send(route, message));
                } else {
                    LOG.warn("No routes - address: {}, message: {}", address, message);
                }
            } else {
                LOG.error("Failed to route - address: {}, message: {}", address, message, ar.cause());
            }
        }));
    }

    protected void load(Handler<AsyncMap<String, String>> handler) {
        if (table != null) {
            handler.handle(table);
        } else {
            vertx.sharedData().<String, String>getAsyncMap(address, ar -> {
                if (ar.succeeded()) {
                    LOG.debug("Loaded - address: {}", address);
                    table = ar.result();
                    handler.handle(table);
                } else {
                    LOG.error("Failed to load - address: {}", address, ar.cause());
                }
            });
        }
    }

    public static String createAddressMember(String address, String route, String member) {
        return String.format("%s/%s/%s", address, route, member);
    }

    public static String createAddressRoute(String address, String route) {
        return String.format("%s/%s", address, route);
    }
}

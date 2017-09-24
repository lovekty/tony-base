package me.tony.base.thrift.spring.server;

import me.tony.base.thrift.spring.config.server.ServerConfig;
import org.apache.thrift.server.TServer;

import java.util.Objects;

public class Server<T> implements Runnable {

    private Class<T> serviceType;

    private Class<?> ifaceType;

    private ServerConfig config;

    private TServer tServer;

    public Server(Class<T> serviceType, ServerConfig config, Object service) {
        this.serviceType = Objects.requireNonNull(serviceType, "service type cannot be null!");
        this.config = Objects.requireNonNull(config, "server config cannot be null!");
        try {
            ifaceType = Class.forName(serviceType.getName() + "$Iface");
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("cannot find Iface for " + serviceType.getName());
        }
        if (!ifaceType.isAssignableFrom(service.getClass())) {
            throw new IllegalArgumentException("service instance is of type:" + service.getClass().getName() + ", not child for " + ifaceType.getName());
        }
    }

    @Override
    public void run() {

    }
}

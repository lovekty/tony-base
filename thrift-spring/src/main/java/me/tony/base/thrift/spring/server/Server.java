package me.tony.base.thrift.spring.server;

import me.tony.base.thrift.spring.config.server.ServerConfig;
import org.apache.thrift.TProcessor;
import org.apache.thrift.server.TServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static me.tony.base.thrift.spring.config.server.ServerConfig.*;

public final class Server {

    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
    private Class<?> serviceType;
    private Class<?> ifaceType;
    private Class<? extends TProcessor> processorType;
    private Object provider;
    private ServerConfig config;
    private TServer server;
    private Thread thread;

    public Server(ServerConfig config, Object provider) {
        this.provider = Objects.requireNonNull(provider, "service type cannot be null!");
        this.config = Objects.requireNonNull(config, "server config cannot be null!");
        ifaceType = getIfaceType(this.provider);
        serviceType = getServiceType(ifaceType);
        processorType = getProcessorType(serviceType);
        server = ServerBuilder.build(this.config, processorType, ifaceType, this.provider);
        thread = new Thread(() -> server.serve(), "ThriftServerThread-" + serviceType.getName());
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            this.stop();
            LOGGER.info("{} stopped", thread.getName());
        }));
    }

    private Class<? extends TProcessor> getProcessorType(Class<?> serviceType) {
        String processorTypeName = serviceType.getName() + PROCESSOR_TAG;
        try {
            Class<?> clazz = Class.forName(processorTypeName);
            if (TProcessor.class.isAssignableFrom(clazz)) {
                return (Class<? extends TProcessor>) clazz;
            }
            throw new IllegalArgumentException(processorTypeName + " is not a TProcessor");
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("cannot find a processor for " + serviceType.getName());
        }
    }

    private Class<?> getIfaceType(Object provider) {
        Class<?> providerType = provider.getClass();
        Class<?>[] interfaces = providerType.getInterfaces();
        for (Class<?> i : interfaces) {
            if (i.getName().endsWith(IFACE_TAG)) {
                return i;
            }
        }
        throw new IllegalArgumentException(provider.getClass().getName() + " is not a thrift iface!");
    }

    private Class<?> getServiceType(Class<?> ifaceType) {
        String ifaceTypeName = ifaceType.getName();
        if (!ifaceTypeName.endsWith(IFACE_TAG)) {
            throw new IllegalArgumentException(ifaceType.getName() + " is not a thrift iface!");
        }
        String serviceTypeName = ifaceTypeName.substring(0, ifaceTypeName.length() - IFACE_TAG_SIZE);
        try {
            return Class.forName(serviceTypeName);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("cannot find base service of type " + serviceTypeName, e);
        }
    }

    public void serve() {
        thread.start();
        LOGGER.info("{} started", server.getClass().getName());
    }

    public void stop() {
        server.stop();
        LOGGER.info("{} stopped", server.getClass().getName());
    }
}

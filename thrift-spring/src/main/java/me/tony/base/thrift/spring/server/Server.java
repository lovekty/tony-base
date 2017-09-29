package me.tony.base.thrift.spring.server;

import me.tony.base.thrift.ThriftCommonUtils;
import me.tony.base.thrift.spring.config.server.ServerConfig;
import org.apache.thrift.TProcessor;
import org.apache.thrift.server.TServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

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
        ifaceType = ThriftCommonUtils.ifaceFromProvider(this.provider);
        serviceType = ThriftCommonUtils.serviceFromIface(ifaceType);
        processorType = ThriftCommonUtils.processorFromService(serviceType);
        server = ServerBuilder.build(this.config, processorType, ifaceType, this.provider);
        thread = new Thread(() -> server.serve(), "ThriftServerThread-" + serviceType.getName());
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            this.stop();
            LOGGER.info("{} stopped", thread.getName());
        }));
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

package me.tony.base.thrift.spring.server.factory;

import me.tony.base.thrift.spring.config.server.ServerConfig;
import org.apache.thrift.server.TServer;

import java.util.Objects;

public abstract class AbstractServerFactory implements ServerFactory {

    @Override
    public TServer buildServer(ServerConfig config) {
        Objects.requireNonNull(config, "config cannot be null!");
        if (supportedServerType() != config.getServerType()) {
            throw new IllegalArgumentException("supported type is: " + supportedServerType() + " but config type is: " + config.getServerType());
        }
        return buildServer(supportedServerType().getServerClass(), config);
    }

    protected abstract TServer buildServer(Class<? extends TServer> serverClass, ServerConfig config);

}

package me.tony.base.thrift.spring.server.factory;

import me.tony.base.thrift.spring.config.server.ServerConfig;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TServerTransport;
import org.springframework.beans.BeanUtils;

import java.util.Objects;

public abstract class AbstractServerFactory implements ServerFactory {

    @Override
    public TServer buildServer(ServerConfig config) {
        Objects.requireNonNull(config, "config cannot be null!");
        if (supportedServerType() != config.getServerType()) {
            throw new IllegalArgumentException("supported type is: " + supportedServerType() + " but config type is: " + config.getServerType());
        }
        TServerTransport transport = createTransport(config);

        return null;
    }

    protected TServerTransport createTransport(ServerConfig config) {
        Class<? extends TServerTransport> transportClass = config.getServerType().getTransportClass();
        try {
            return BeanUtils.instantiateClass(transportClass.getDeclaredConstructor(new Class<?>[]{Integer.TYPE, Integer.TYPE}),
                    config.getPort(), config.getTimeout());
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("cannot find target constructor!", e);
        }
    }

//    protected abstract TServer buildServer(Class<? extends TServer> serverClass, ServerConfig config);

}

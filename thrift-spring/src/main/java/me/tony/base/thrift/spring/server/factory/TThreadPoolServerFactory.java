package me.tony.base.thrift.spring.server.factory;

import me.tony.base.thrift.spring.config.server.ServerConfig;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TServerTransport;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class TThreadPoolServerFactory extends AbstractServerFactory {
    @Override
    protected TServer buildServer(Class<? extends TServer> serverClass, ServerConfig config) {
        try {
            Constructor<? extends TServerTransport> transportConstructor = config.getServerType().getTransportClass().getDeclaredConstructor();
            ReflectionUtils.makeAccessible(transportConstructor);
            TServerTransport transport = transportConstructor.newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            //TODO
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public ServerConfig.ServerType supportedServerType() {
        return ServerConfig.ServerType.POOL;
    }
}

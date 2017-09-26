package me.tony.base.thrift.spring.server.factory;

import me.tony.base.thrift.spring.config.server.ServerConfig;
import org.apache.thrift.server.TServer;

public interface ServerFactory<T extends TServer> {

    T buildServer(ServerConfig config, Object provider);

    ServerConfig.ServerType supportedServerType();
}

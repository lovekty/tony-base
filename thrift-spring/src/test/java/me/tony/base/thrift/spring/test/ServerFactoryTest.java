package me.tony.base.thrift.spring.test;

import me.tony.base.thrift.spring.config.server.ServerConfig;
import me.tony.base.thrift.spring.server.factory.AbstractServerFactory;
import org.apache.thrift.transport.TServerTransport;
import org.junit.Assert;
import org.junit.Test;

public class ServerFactoryTest {

    @Test
    public void testCreateTransport() {
        ServerConfig config = new ServerConfig();
        config.setPort(10000);
        config.setTimeout(100);
        config.setServerType(ServerConfig.ServerType.SELECTOR);
        Factory factory = new Factory();
        TServerTransport entity = factory.testCreateTransport(config);
        Assert.assertNotNull(entity);
    }
}

class Factory extends AbstractServerFactory {

    public TServerTransport testCreateTransport(ServerConfig config) {
        return this.createTransport(config);
    }

    @Override
    public ServerConfig.ServerType supportedServerType() {
        return null;
    }
}
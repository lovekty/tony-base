package me.tony.base.thrift.spring.test;

import me.tony.base.thrift.gen.DemoService;
import me.tony.base.thrift.spring.config.protocol.ProtocolConfig;
import me.tony.base.thrift.spring.config.server.ServerConfig;
import me.tony.base.thrift.spring.server.Server;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.junit.Test;

public class ServerFactoryTest {

    public static void main(String[] args) {
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setProtocolType(ProtocolConfig.ProtocolType.BINARY);
        ServerConfig config = new ServerConfig();
        config.setPort(10000);
        config.setTimeout(100);
        config.setServerType(ServerConfig.ServerType.POOL);
        config.setProtocol(protocolConfig);
        Server server = new Server(config, new DemoServiceImpl());
        server.serve();
    }

    @Test
    public void testClient() throws TException {
        TTransport transport = new TSocket("localhost", 10000, 20);
        transport.open();
        DemoService.Client client = new DemoService.Client(new TBinaryProtocol(transport));
        System.out.println(client.getName("Tony"));
    }

    @Test
    public void test() {
        String name = "com.foo.bar.BaseService$Iface";
        System.out.println(name.substring(0, name.length() - "$Iface".length()));
    }
}

class DemoServiceImpl implements DemoService.Iface {

    @Override
    public String getName(String prefix) throws TException {
        return prefix + " is God";
    }

    @Override
    public int getAge() throws TException {
        return 999;
    }
}
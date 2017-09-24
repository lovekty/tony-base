package me.tony.base.thrift.spring.config.server;

public class ThreadPoolServerConfig extends ServerConfig {
    private static final long serialVersionUID = -5062627726295376922L;

    @Override
    public ServerType getServerType() {
        return ServerType.POOL;
    }

    @Override
    public void setServerType(ServerType serverType) {
        super.setServerType(ServerType.POOL);
    }
}

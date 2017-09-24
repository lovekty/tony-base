package me.tony.base.thrift.spring.config.client;

import me.tony.base.thrift.spring.config.protocol.ProtocolConfig;

import java.io.Serializable;

public class ClientConfig implements Serializable {
    private static final long serialVersionUID = 6904961053130662378L;

    private String host;
    private int port;
    private int timeout;
    private ProtocolConfig protocol;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public ProtocolConfig getProtocol() {
        return protocol;
    }

    public void setProtocol(ProtocolConfig protocol) {
        this.protocol = protocol;
    }
}

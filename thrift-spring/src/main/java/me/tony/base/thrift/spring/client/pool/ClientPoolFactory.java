package me.tony.base.thrift.spring.client.pool;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.thrift.TServiceClient;

import java.util.Objects;

import static me.tony.base.thrift.spring.config.ConfigConstants.CLIENT_TAG;
import static me.tony.base.thrift.spring.config.server.ServerConfig.IFACE_TAG;

public class ClientPoolFactory<T extends TServiceClient> extends BasePooledObjectFactory<T> {

    private Class<?> serviceType;
    private Class<?> ifaceType;
    private Class<? extends TServiceClient> clientType;

    public ClientPoolFactory(Class<?> serviceType) {
        this.serviceType = Objects.requireNonNull(serviceType, "service type cannot be null!");
        try {
            this.ifaceType = ClientPoolFactory.class.getClassLoader().loadClass(this.serviceType.getName() + IFACE_TAG);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("cannot find iface for " + serviceType.getName());
        }
        try {
            Class<?> clazz = ClientPoolFactory.class.getClassLoader().loadClass(this.serviceType.getName() + CLIENT_TAG);
            if (TServiceClient.class.isAssignableFrom(clazz)) {
                this.clientType = (Class<? extends TServiceClient>) clazz;
            } else {
                throw new IllegalArgumentException(clazz.getName() + " is not a thrift service client!");
            }
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("cannot find client for " + serviceType.getName());
        }
    }

    @Override
    public T create() throws Exception {
        return null;
    }

    @Override
    public PooledObject<T> wrap(T obj) {
        return null;
    }
}

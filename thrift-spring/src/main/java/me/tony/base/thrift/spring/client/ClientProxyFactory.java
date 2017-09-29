package me.tony.base.thrift.spring.client;

public class ClientProxyFactory<T> {

    private Class<T> serviceType;

    public ClientProxyFactory(Class<T> serviceType) {
        this.serviceType = serviceType;
    }

//    private Class<?> getIfaceType()
}

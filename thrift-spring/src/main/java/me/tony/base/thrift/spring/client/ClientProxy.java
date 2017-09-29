package me.tony.base.thrift.spring.client;

import me.tony.base.thrift.ThriftCommonUtils;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.TServiceClientFactory;
import org.apache.thrift.protocol.TProtocol;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ClientProxy<T> implements InvocationHandler, Serializable {
    private static final long serialVersionUID = 4257547906234924139L;

    private Class<T> serviceType;
    private Class<?> ifaceType;
    private Class<? extends TServiceClientFactory> clientFactoryType;
    private TServiceClientFactory clientFactory;
    private TServiceClient client;
    private ConcurrentHashMap<String, TServiceClient> clientMap;

    public ClientProxy(Class<T> serviceType, ConcurrentHashMap<String, TServiceClient> clientMap) {
        this.serviceType = Objects.requireNonNull(serviceType, "service type cannot be null!");
        this.clientMap = Objects.requireNonNull(clientMap, "client map cannot be null!");
        this.ifaceType = ThriftCommonUtils.ifaceFromService(serviceType);
        this.clientFactoryType = ThriftCommonUtils.clientFactoryFromService(serviceType);
        this.clientFactory = BeanUtils.instantiateClass(clientFactoryType);
        this.client = this.clientFactory.getClient(determinProtocol());
    }

    protected abstract TProtocol determinProtocol();

    private TServiceClient getClient(String key) {
        clientMap.putIfAbsent(key, this.clientFactory.getClient())
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            if (Object.class.equals(method.getDeclaringClass())) {
                return method.invoke(this, args);
            } else if (method.isDefault()) {
                return invokeDefaultMethod(proxy, method, args);
            }
        } catch (Throwable t) {
            throw ExceptionUtil.unwrapThrowable(t);
        }
        return null;
    }

    private Object invokeDefaultMethod(Object proxy, Method method, Object[] args)
            throws Throwable {
        final Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class
                .getDeclaredConstructor(Class.class, int.class);
        if (!constructor.isAccessible()) {
            constructor.setAccessible(true);
        }
        final Class<?> declaringClass = method.getDeclaringClass();
        return constructor.newInstance(declaringClass, MethodHandles.Lookup.PRIVATE)
                .unreflectSpecial(method, declaringClass).bindTo(proxy).invokeWithArguments(args);
    }
}

package me.tony.base.thrift.spring.server.factory;

import me.tony.base.thrift.spring.config.server.ServerConfig;
import org.apache.thrift.TProcessor;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TServerTransport;
import org.springframework.beans.BeanUtils;

import java.util.Objects;

public abstract class AbstractServerFactory implements ServerFactory {

    @Override
    public TServer buildServer(ServerConfig config, Object provider) {
        Objects.requireNonNull(config, "config cannot be null!");
        Objects.requireNonNull(provider, "provider connot be null!");
        Class<?> definedServiceClass = getDefinedServiceClass(provider);
        if (supportedServerType() != config.getServerType()) {
            throw new IllegalArgumentException("supported type is: " + supportedServerType() + " but config type is: " + config.getServerType());
        }
        TServerTransport transport = createTransport(config);
        TServer.AbstractServerArgs args = createArgs(config);
        TProcessor processor = createProcessor(provider);
        Class<? extends TServer> serverClass = config.getServerType().getServerClass();
        try {
            serverClass.getConstructor();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }

    private TProcessor createProcessor(Object provider) {
        Class<?> definedServiceClass = getDefinedServiceClass(provider);
        Class<? extends TProcessor> processorClass;
        try {
            Class<?> originProcessorClass = Class.forName(definedServiceClass.getName()+"$Processor");
            if (!TProcessor.class.isAssignableFrom(originProcessorClass)) {
                throw new RuntimeException(originProcessorClass.getName()+" is not a TProcessor!");
            }
            processorClass = (Class<? extends TProcessor>) originProcessorClass;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    Class<?> getDefinedServiceClass(Object provider) {
        Class<?> providerClass = provider.getClass();
        Class<?>[] interfaces = providerClass.getInterfaces();
        Class<?> definedServiceClass = null;
        for (Class<?> iface : interfaces) {
            String ifaceClassName;
            if ((ifaceClassName = iface.getName()).endsWith("$Iface")) {
                try {
                    definedServiceClass = Class.forName(ifaceClassName.substring(0, ifaceClassName.length() - "$Iface".length()));
                } catch (ClassNotFoundException e) {
                    continue;
                }
            }
        }
        if (definedServiceClass == null) {
            throw new RuntimeException("class " + provider.getClass().getName() + " is not a thrift iface provider");
        }
        return definedServiceClass
    }

    protected abstract TServer.AbstractServerArgs createArgs(ServerConfig config);

    protected TServerTransport createTransport(ServerConfig config) {
        Class<? extends TServerTransport> transportClass = config.getServerType().getTransportClass();
        try {
            return BeanUtils.instantiateClass(transportClass.getConstructor(Integer.TYPE, Integer.TYPE),
                    config.getPort(), config.getTimeout());
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("cannot find target constructor for " + transportClass.getName(), e);
        }
    }

//    protected abstract TServer buildServer(Class<? extends TServer> serverClass, ServerConfig config);

}

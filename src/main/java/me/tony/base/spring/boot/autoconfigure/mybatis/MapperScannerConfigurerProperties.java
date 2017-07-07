package me.tony.base.spring.boot.autoconfigure.mybatis;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Created by tony on 2017/7/7.
 */
@ConfigurationProperties("tony.base.mybatis.mapperscanner")
public class MapperScannerConfigurerProperties {
    private List<Configurer> configurers;

    public List<Configurer> getConfigurers() {
        return configurers;
    }

    public void setConfigurers(List<Configurer> configurers) {
        this.configurers = configurers;
    }
}

class Configurer {
    private String sqlSessionFactoryBeanName;

    private String basePackage;

    public String getSqlSessionFactoryBeanName() {
        return sqlSessionFactoryBeanName;
    }

    public void setSqlSessionFactoryBeanName(String sqlSessionFactoryBeanName) {
        this.sqlSessionFactoryBeanName = sqlSessionFactoryBeanName;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }
}

package com.example.api.config;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import jakarta.annotation.Resource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(MybatisPlusProperties.class)
@ConditionalOnClass({SqlSessionFactory.class, MybatisSqlSessionFactoryBean.class})
public class MybatisPlusConfig {

    @Resource
    private MybatisPlusProperties properties;

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean factory = new MybatisSqlSessionFactoryBean();
        factory.setDataSource(dataSource);

        MybatisConfiguration configuration = new MybatisConfiguration();
        MybatisPlusProperties.CoreConfiguration coreConfig = properties.getConfiguration();
        if (coreConfig != null) {
            if (coreConfig.getMapUnderscoreToCamelCase() != null) {
                configuration.setMapUnderscoreToCamelCase(coreConfig.getMapUnderscoreToCamelCase());
            }
            if (coreConfig.getLogImpl() != null) {
                configuration.setLogImpl(coreConfig.getLogImpl());
            }
        }
        factory.setConfiguration(configuration);

        return factory.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}


package com.example.onlineshoppingmall.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.example.onlineshoppingmall", sqlSessionFactoryRef = "mariaSqlSessionFactory")
public class MainDataSourceConfig {

    @Bean(name = "mariaDataSource")
    @Primary // 기본 데이터소스로 설정
    @ConfigurationProperties(prefix = "spring.datasource.mariadb")
    public DataSource mariaDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "mariaSqlSessionFactory")
    public SqlSessionFactory mariaSqlSessionFactory(@Qualifier("mariaDataSource") DataSource mariaDataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(mariaDataSource);
        sqlSessionFactoryBean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:mybatis-config.xml"));
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
        sqlSessionFactoryBean.setTypeAliasesPackage("com.example.onlineshoppingmall");
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "mariaSqlSessionTemplate")
    public SqlSessionTemplate mariaSqlSessionTemplate(@Qualifier("mariaSqlSessionFactory") SqlSessionFactory mariaSqlSessionFactory) {
        return new SqlSessionTemplate(mariaSqlSessionFactory);
    }

}

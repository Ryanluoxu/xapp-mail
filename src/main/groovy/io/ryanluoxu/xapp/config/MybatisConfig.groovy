package io.ryanluoxu.xapp.config

import groovy.transform.CompileStatic
import org.apache.ibatis.logging.slf4j.Slf4jImpl
import org.apache.ibatis.session.SqlSessionFactory
import org.apache.ibatis.type.EnumTypeHandler
import org.mybatis.spring.SqlSessionFactoryBean
import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import javax.sql.DataSource

/**
 * @author luoxu on 10/19
 */
@CompileStatic
@Configuration
@MapperScan(basePackages = ['io.ryanluoxu.xapp.mapper'])
class MybatisConfig {

    @Bean
    @ConfigurationProperties("spring.datasource")
    DataSource dataSource(){
        return DataSourceBuilder.create().build()
    }

    @Bean
    SqlSessionFactory sqlSessionFactory(ApplicationContext context, DataSource dataSource){
        org.apache.ibatis.session.Configuration mybatisConfig = new org.apache.ibatis.session.Configuration(
                logImpl: Slf4jImpl,
                mapUnderscoreToCamelCase: true,
                defaultEnumTypeHandler: EnumTypeHandler
        )
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean(
                configuration: mybatisConfig,
                dataSource: dataSource
        )
        return sessionFactoryBean.getObject()
    }
}

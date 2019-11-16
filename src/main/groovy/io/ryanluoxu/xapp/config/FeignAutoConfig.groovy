package io.ryanluoxu.xapp.config

import feign.Client
import feign.Request
import feign.okhttp.OkHttpClient
import groovy.transform.CompileStatic
import okhttp3.ConnectionPool
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.cloud.commons.httpclient.OkHttpClientConnectionPoolFactory
import org.springframework.cloud.commons.httpclient.OkHttpClientFactory
import org.springframework.cloud.openfeign.support.FeignHttpClientProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import javax.annotation.PreDestroy
import java.util.concurrent.TimeUnit

@CompileStatic
class FeignAutoConfig {

    @Configuration
    @ConditionalOnClass(OkHttpClient.class)
    @ConditionalOnMissingClass("com.netflix.loadbalance.ILoadBalancer")
    @ConditionalOnProperty(value = "feign.okhttp.enable")
    protected static class OkHttpFeignConfig{
        private okhttp3.OkHttpClient okHttpClient

        @Bean
        @ConditionalOnMissingBean(Client.class)
        Client feignClient(okhttp3.OkHttpClient okHttpClient){
            if (this.okHttpClient==null){
                this.okHttpClient= okHttpClient
            }
            return new OkHttpClient(this.okHttpClient)
        }

        @Bean
        @ConditionalOnMissingBean(okhttp3.OkHttpClient.class)
        okhttp3.OkHttpClient client(OkHttpClientFactory factory, ConnectionPool connectionPool, FeignHttpClientProperties properties){
            Boolean followRedirects = properties.isFollowRedirects()    // true
            Integer connectionTimeout = properties.getConnectionTimeout()   //  2s
            Boolean disableSslValidation = properties.isDisableSslValidation()  // false
            this.okHttpClient = factory.createBuilder(disableSslValidation)
                    .connectTimeout(connectionTimeout, TimeUnit.MILLISECONDS)
                    .followRedirects(followRedirects)
                    .connectionPool(connectionPool)
                    .build()
            return this.okHttpClient
        }

        @Bean
        @ConditionalOnMissingBean(ConnectionPool.class)
        ConnectionPool httpClientConnectionPool(FeignHttpClientProperties properties, OkHttpClientConnectionPoolFactory factory){
            Integer maxTotalConnection = properties.getMaxConnections()     // 200
            Long timeToLive = properties.getTimeToLive()    // 900
            TimeUnit ttlUnit = properties.getTimeToLiveUnit()   // s
            return factory.create(maxTotalConnection, timeToLive, ttlUnit)
        }

        /**
         * before destroy, release all resource
         */
        @PreDestroy
        void destroy(){
            if (okHttpClient!=null){
                if (!okHttpClient.dispatcher().executorService().isShutdown()){
                    okHttpClient.dispatcher().executorService().shutdown()
                }
                okHttpClient.connectionPool().evictAll()
            }
        }

        /**
         * set timeout option
         */
        @Bean
        Request.Options options(){
            return new Request.Options(5000,5000)
        }

    }
}

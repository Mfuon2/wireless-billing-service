package com.softel.mpesa.config
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Bean
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer
import java.time.Duration
import org.springframework.data.redis.cache.RedisCacheConfiguration


@Configuration
class RedisConfig {

    @Bean
    fun redisCacheManagerBuilderCustomizer() = RedisCacheManagerBuilderCustomizer { builder ->
        val configurationMap = HashMap<String, RedisCacheConfiguration>()
        configurationMap["cambiumToken"] = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(7200))
        //configurationMap["cache2"] = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(2))
        builder.withInitialCacheConfigurations(configurationMap)
    }
}
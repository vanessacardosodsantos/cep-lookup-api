package com.br.cep.lookup.api.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {

        CaffeineCache cacheCep = buildCache(
                "enderecos",
                500,// máximo 500 CEPs em memória
                60 * 24 // TTL de 24 horas em minutos
        );

        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(List.of(cacheCep));
        return manager;
    }

    private CaffeineCache buildCache(String nome, int tamanhoMaximo, long ttlMinutos) {
        Cache<Object, Object> cache = Caffeine.newBuilder()
                .maximumSize(tamanhoMaximo)
                .expireAfterWrite(ttlMinutos, TimeUnit.MINUTES)
                .recordStats()// recordStats() habilita coleta de métricas
                .build();
        return new CaffeineCache(nome, cache);
    }
}

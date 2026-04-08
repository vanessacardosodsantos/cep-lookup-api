package com.br.cep.lookup.api.service;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CacheStatsService {
    private final CacheManager cacheManager;

    public Map<String, Object> buscarStats() {
        Map<String, Object> resultado = new HashMap<>();

        cacheManager.getCacheNames().forEach(nome -> {
            CaffeineCache caffeineCache = (CaffeineCache) cacheManager.getCache(nome);
            Cache<Object, Object> nativeCache = caffeineCache.getNativeCache();
            com.github.benmanes.caffeine.cache.stats.CacheStats stats = nativeCache.stats();

            String hitRate = stats.requestCount() == 0
                    ? "N/A — nenhuma requisição ainda"
                    : String.format("%.1f%%", stats.hitRate() * 100);

            resultado.put(nome, Map.of(
                    "hits", stats.hitCount(), // quantas vezes o dado foi encontrado no cache
                    "misses", stats.missCount(), // quantas vezes foi ao ViaCEP porque não estava no cache
                    "hitRate", hitRate, // porcentagem de requests servidas do cache ex: "94.3%" significa que só 5.7% foram à ViaCEP
                    "tamanhoAtual", nativeCache.estimatedSize(), // quantos CEPs estão em memória agora
                    "evictions", stats.evictionCount()// quantos foram removidos por TTL ou por atingir maximumSize
            ));
        });

        return resultado;
    }

    public void limparTodos() {
        cacheManager.getCacheNames()
                .forEach(nome -> cacheManager.getCache(nome).clear());
    }
}

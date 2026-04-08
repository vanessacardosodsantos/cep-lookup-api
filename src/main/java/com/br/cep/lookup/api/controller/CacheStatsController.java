package com.br.cep.lookup.api.controller;

import com.br.cep.lookup.api.service.CacheStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/cache")
@RequiredArgsConstructor
public class CacheStatsController {
    private final CacheManager cacheManager;

    private final CacheStatsService cacheStatsService;

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> stats() {
        return ResponseEntity.ok(cacheStatsService.buscarStats());
    }

    @GetMapping("/clear")
    public ResponseEntity<String> clear() {
        cacheStatsService.limparTodos();
        return ResponseEntity.ok("Todos os caches foram limpos");
    }
}

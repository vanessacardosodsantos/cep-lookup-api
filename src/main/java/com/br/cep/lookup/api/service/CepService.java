package com.br.cep.lookup.api.service;

import com.br.cep.lookup.api.dto.EnderecoDTO;
import com.br.cep.lookup.api.client.ViaCepClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CepService {
    private final ViaCepClient viaCepClient;

    @Cacheable(value = "enderecos", key = "#cep")
    public EnderecoDTO buscarEndereco(String cep) {
        String cepLimpo = limparCep(cep);
        log.info("[SERVICE] Cache miss para CEP: {}. Buscando na ViaCEP...", cepLimpo);

        EnderecoDTO endereco = viaCepClient.buscarCep(cepLimpo);
        endereco.setFromCache(false);

        return endereco;
    }


    private String limparCep(String cep) {
        if (cep == null || cep.isBlank()) {
            throw new IllegalArgumentException("CEP não pode ser vazio");
        }
        String limpo = cep.replaceAll("[^0-9]", "");
        if (limpo.length() != 8) {
            throw new IllegalArgumentException("CEP deve conter 8 dígitos: " + cep);
        }
        return limpo;
    }
}

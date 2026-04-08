package com.br.cep.lookup.api.client;

import com.br.cep.lookup.api.dto.EnderecoDTO;
import com.br.cep.lookup.api.exception.CepNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class ViaCepClient {
    private final RestClient restClient;

    public ViaCepClient() {
        this.restClient = RestClient.builder()
                .baseUrl("https://viacep.com.br/ws")
                .build();
    }

    public EnderecoDTO buscarCep(String cep) {
        log.info("[VIACEP] Chamando API externa para CEP: {}", cep);

        try {
            EnderecoDTO endereco = restClient.get()
                    .uri("/{cep}/json", cep)
                    .retrieve()
                    .body(EnderecoDTO.class);

            if (endereco == null || endereco.getCep() == null) {
                throw new CepNotFoundException(cep);
            }

            log.info("[VIACEP] CEP encontrado: {} - {}", cep, endereco.getLogradouro());
            return endereco;

        } catch (HttpClientErrorException.BadRequest e) {
            throw new IllegalArgumentException("CEP inválido: " + cep);
        }
    }
}

package com.br.cep.lookup.api.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EnderecoDTO {

        @JsonProperty("cep")
        private String cep;

        @JsonProperty("logradouro")
        private String logradouro;

        @JsonProperty("complemento")
        private String complemento;

        @JsonProperty("bairro")
        private String bairro;

        @JsonProperty("localidade")
        private String cidade;

        @JsonProperty("uf")
        private String estado;

        @JsonProperty("ibge")
        private String codigoIbge;

        private boolean fromCache;
    }

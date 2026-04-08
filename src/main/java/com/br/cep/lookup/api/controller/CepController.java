package com.br.cep.lookup.api.controller;

import com.br.cep.lookup.api.dto.EnderecoDTO;
import com.br.cep.lookup.api.service.CepService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cep")
@RequiredArgsConstructor
public class CepController {
    private final CepService cepService;

    @GetMapping("/{cep}")
    public ResponseEntity<EnderecoDTO> buscarEndereco(@PathVariable String cep) {
        return ResponseEntity.ok(cepService.buscarEndereco(cep));
    }
}

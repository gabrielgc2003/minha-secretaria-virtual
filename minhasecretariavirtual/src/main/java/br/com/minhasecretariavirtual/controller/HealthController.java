package br.com.minhasecretariavirtual.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {

    /**
     * Endpoint simples para verificar se
     * a aplicação está rodando.
     */
    @GetMapping
    public String health() {
        return "OK";
    }
}

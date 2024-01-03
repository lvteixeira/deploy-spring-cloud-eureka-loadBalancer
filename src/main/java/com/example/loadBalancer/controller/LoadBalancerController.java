package com.example.loadBalancer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api-gateway")
public class LoadBalancerController {
    @Autowired
    private WebClient.Builder webClientBuilder;

    public LoadBalancerController(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @GetMapping("{echo}")
    public Mono<?> sendEcho(@PathVariable("echo") String echo) {
        return Mono.fromSupplier(() -> {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(echo);
        });
    }

    @GetMapping("ananda")
    public Mono<?> routeToAnandaService(@RequestBody Object requestDTO) {
        WebClient client = webClientBuilder.baseUrl("http://ananda").build();
        return client.post()
                .uri("/payment")
                .bodyValue(requestDTO)
                .exchangeToMono(response -> {
                    if (!response.statusCode().is2xxSuccessful()) {
                        return response.createException()
                                .flatMap(Mono::error);
                    }
                    return response.bodyToMono(Object.class);
                })
                .onErrorResume(e -> Mono.just(ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Service call failed: "+e.getMessage())
                ));
    }

    @GetMapping("zyra")
    public Mono<?> routeToZyraService(@RequestBody Object requestDTO) {
        WebClient client = webClientBuilder.baseUrl("http://zyra").build();
        return client.post()
                .uri("/pedido")
                .bodyValue(requestDTO)
                .exchangeToMono(response -> {
                    if (!response.statusCode().is2xxSuccessful()) {
                        return response.createException()
                                .flatMap(Mono::error);
                    }
                    return response.bodyToMono(Object.class);
                })
                .onErrorResume(e -> Mono.just(ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Service call failed: "+e.getMessage())

                ));
    }

    @GetMapping("jet")
    public Mono<?> routeToJetService(@RequestBody Object requestDTO) {
        WebClient client = webClientBuilder.baseUrl("http://jet").build();
        return client.post()
                .uri("/shipment")
                .bodyValue(requestDTO)
                .exchangeToMono(response -> {
                    if (!response.statusCode().is2xxSuccessful()) {
                        return response.createException()
                                .flatMap(Mono::error);
                    }
                    return response.bodyToMono(Object.class);
                })
                .onErrorResume(e -> Mono.just(ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Service call failed: "+e.getMessage())

                ));
    }
}
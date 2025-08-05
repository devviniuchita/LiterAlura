package br.com.gutendex.api.service;

import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class ConsumoApi {
    public String obterDados(String endereco) {
        try {
            if (endereco.startsWith("http://")) {
                endereco = endereco.replace("http://", "https://");
            }

            HttpClient client = HttpClient.newBuilder()
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endereco))
                    .timeout(Duration.ofSeconds(10))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


            if (response.statusCode() >= 300) {
                throw new RuntimeException("API retornou status " + response.statusCode() +
                        "\nURL acessada: " + endereco +
                        "\nResposta: " + response.body());
            }

            return response.body();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao consumir API: " + e.getMessage());
        }
    }
}

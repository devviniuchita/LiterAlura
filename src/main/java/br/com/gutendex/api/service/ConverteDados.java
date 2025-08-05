package br.com.gutendex.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ConverteDados {
    private ObjectMapper mapper = new ObjectMapper();

    public <T> T obterDados(String json, Class<T> classe) {
            try {
                return mapper.readValue(json, classe);
            } catch (IOException e) {
                throw new RuntimeException("Erro ao converter JSON: " + e.getMessage(), e);
            }
        }



    }

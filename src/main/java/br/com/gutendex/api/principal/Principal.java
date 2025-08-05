package br.com.gutendex.api.principal;

import br.com.gutendex.api.ConsumoApi;
import br.com.gutendex.api.ConverteDados;
import br.com.gutendex.api.dto.GutendexResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Scanner;

public class Principal {
    private Scanner scanner = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String URL_BASE = "https://gutendex.com/books?search=";

    public void exibeMenu() throws JsonProcessingException {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("""
                    
                    === MENU ===
                    1 - Buscar livro pelo título
                    2 - Listar livros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos em um ano
                    5 - Listar livros por idioma
                    0 - Sair
                    """);

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1 -> {
                    buscarLivroViaApi();
                }
                case 2 -> System.out.println("Em breve: listar livros...");
                case 3 -> System.out.println("Em breve: listar autores...");
                case 4 -> System.out.println("Em breve: autores vivos em um ano...");
                case 5 -> System.out.println("Em breve: livros por idioma...");
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private void buscarLivroViaApi() {
        System.out.println("Digite o título do livro:");
        var titulo = scanner.nextLine();
        var url = URL_BASE + titulo.replace(" ", "%20");

        try {
            var json = consumoApi.obterDados(url);
            var resposta = conversor.obterDados(json, GutendexResponseDTO.class);

            System.out.println("\nLivros encontrados:");
            resposta.getResults().forEach(livro -> {
                System.out.println("---- LIVRO ----");
                System.out.println("Título: " + livro.getTitle());

                String idioma = (livro.getLanguages() != null && !livro.getLanguages().isEmpty())
                        ? livro.getLanguages().get(0) : "Idioma desconhecido";
                System.out.println("Idioma: " + idioma);

                System.out.println("Downloads: " + livro.getDownloadCount());

                if (livro.getAuthors() != null && !livro.getAuthors().isEmpty()) {
                    var autor = livro.getAuthors().get(0);
                    System.out.println("Autor: " + autor.getName());
                    if (autor.getBirthYear() != null && autor.getDeathYear() != null) {
                        System.out.println("Nascimento: " + autor.getBirthYear() +
                                ", Morte: " + autor.getDeathYear());
                    }
                }
                System.out.println("_______________");
            });
        } catch (Exception e) {
            System.out.println("Erro na busca: " + e.getMessage());
            e.printStackTrace();
        }
    }

 }


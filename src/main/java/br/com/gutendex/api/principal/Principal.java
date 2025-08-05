package br.com.gutendex.api.principal;

import br.com.gutendex.api.service.ConsumoApi;
import br.com.gutendex.api.service.ConverteDados;
import br.com.gutendex.api.dto.GutendexResponseDTO;
import br.com.gutendex.api.dto.AuthorDTO;
import br.com.gutendex.api.dto.BookDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Scanner;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;

public class Principal {
    private Scanner scanner = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String URL_BASE = "https://gutendex.com/books/";
    private GutendexResponseDTO dadosCache;

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
                case 1 -> buscarLivroViaApi();
                case 2 -> listarLivrosRegistrados();
                case 3 -> listarAutoresRegistrados();
                case 4 -> listarAutoresVivosEmAno();
                case 5 -> listarLivrosPorIdioma();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private void buscarLivroViaApi() {
        System.out.println("Digite o título do livro:");
        var titulo = scanner.nextLine();
        var url = URL_BASE + "?search=" + titulo.replace(" ", "%20");
        buscarDados(url);
    }

    private void listarLivrosRegistrados() {
        var url = URL_BASE + "?page=1";
        buscarDados(url);
    }

    private void listarAutoresRegistrados() {
        var url = URL_BASE + "?page=1";
        try {
            var json = consumoApi.obterDados(url);
            var resposta = conversor.obterDados(json, GutendexResponseDTO.class);
            dadosCache = resposta;

            System.out.println("\n=== AUTORES REGISTRADOS ===");
            resposta.getResults().stream()
                    .flatMap(livro -> livro.getAuthors().stream())
                    .distinct()
                    .sorted(Comparator.comparing(AuthorDTO::getName))
                    .forEach(autor -> {
                        System.out.println("---- AUTOR ----");
                        System.out.println("Nome: " + autor.getName());
                        if (autor.getBirthYear() != null && autor.getDeathYear() != null) {
                            System.out.println("Nascimento: " + autor.getBirthYear() +
                                    ", Morte: " + autor.getDeathYear());
                        }
                        System.out.println("_______________");
                    });
        } catch (Exception e) {
            System.out.println("Erro na busca: " + e.getMessage());
        }
    }

    private void listarAutoresVivosEmAno() {
        System.out.println("Digite o ano para verificar autores vivos:");
        try {
            int ano = scanner.nextInt();
            scanner.nextLine();

            if (dadosCache == null) {
                var url = URL_BASE + "?page=1";
                var json = consumoApi.obterDados(url);
                dadosCache = conversor.obterDados(json, GutendexResponseDTO.class);
            }

            System.out.println("\n=== AUTORES VIVOS EM " + ano + " ===");
            dadosCache.getResults().stream()
                    .flatMap(livro -> livro.getAuthors().stream())
                    .distinct()
                    .filter(autor -> autor.getBirthYear() != null &&
                            autor.getDeathYear() != null &&
                            autor.getBirthYear() <= ano &&
                            autor.getDeathYear() >= ano)
                    .forEach(autor -> {
                        System.out.println("---- AUTOR ----");
                        System.out.println("Nome: " + autor.getName());
                        System.out.println("Nascimento: " + autor.getBirthYear() +
                                ", Morte: " + autor.getDeathYear());
                        System.out.println("Idade em " + ano + ": " +
                                (ano - autor.getBirthYear()) + " anos");
                        System.out.println("_______________");
                    });
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void listarLivrosPorIdioma() {
        System.out.println("Digite o idioma para filtrar (ex: pt, en, fr):");
        var idioma = scanner.nextLine().toLowerCase();

        var url = URL_BASE + "?page=1";
        try {
            var json = consumoApi.obterDados(url);
            var resposta = conversor.obterDados(json, GutendexResponseDTO.class);
            dadosCache = resposta;

            System.out.println("\n=== LIVROS EM " + idioma.toUpperCase() + " ===");
            resposta.getResults().stream()
                    .filter(livro -> livro.getLanguages() != null &&
                            livro.getLanguages().contains(idioma))
                    .forEach(livro -> {
                        System.out.println("---- LIVRO ----");
                        System.out.println("Título: " + livro.getTitle());
                        System.out.println("Idioma: " + idioma);
                        System.out.println("Downloads: " + livro.getDownloadCount());
                        if (!livro.getAuthors().isEmpty()) {
                            var autor = livro.getAuthors().get(0);
                            System.out.println("Autor: " + autor.getName());
                        }
                        System.out.println("_______________");
                    });
        } catch (Exception e) {
            System.out.println("Erro na busca: " + e.getMessage());
        }
    }

    private void buscarDados(String url) {
        try {
            var json = consumoApi.obterDados(url);
            var resposta = conversor.obterDados(json, GutendexResponseDTO.class);
            dadosCache = resposta;

            System.out.println("\nResultados encontrados:");
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
        }
    }
}
package br.com.gutendex.api.principal;

import br.com.gutendex.api.service.ConsumoApi;
import br.com.gutendex.api.service.ConverteDados;
import br.com.gutendex.api.dto.GutendexResponseDto;
import br.com.gutendex.api.dto.AuthorDTO;
import br.com.gutendex.api.dto.BookDTO;
import br.com.gutendex.api.model.Author;
import br.com.gutendex.api.model.Book;
import br.com.gutendex.api.repository.AuthorRepository;
import br.com.gutendex.api.repository.BookRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Principal {
    private final Scanner scanner = new Scanner(System.in);

    @Autowired
    private ConsumoApi consumoApi;
    @Autowired
    private ConverteDados conversor;
    private final String URL_BASE = "https://gutendex.com/books/";

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    private List<BookDTO> allFoundBooks = new ArrayList<>();
    private List<BookDTO> lastSearchResults = new ArrayList<>();

    public void exibeMenu() throws JsonProcessingException {
        int option = -1;

        while (option != 0) {
            System.out.println("""
                    
                    === MENU ===
                    1 - Buscar livro por título
                    2 - Listar livros registrado (últila busca)
                    3 - Listar autores registrados
                    4 - listar autores vivos em determinado ano
                    5 - Listar livros por idioma (ex: pt, en, fr, etc.)
                    0 - Sair
                    """);

            try {
                option = scanner.nextInt();
                scanner.nextLine();

                switch (option) {
                    case 1 -> searchBookByTitle();
                    case 2 -> listRegisteredBooks();
                    case 3 -> listRegisteredAuthors();
                    case 4 -> listAuthorsAliveInYear();
                    case 5 -> listBooksByLanguage();
                    case 0 -> System.out.println("Saindo...");
                    default -> System.out.println("Opção invalida.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Por favor, insira um número válido.");
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Ocorreu um erro: " + e.getMessage());
                scanner.nextLine();
            }
        }
    }

    private void searchBookByTitle() {
        System.out.println("Digite o título do livro:");
        var title = scanner.nextLine();
        var url = URL_BASE + "?search=" + title.toLowerCase().replace(" ", "%20");

        try {
            var json = consumoApi.obterDados(url);
            var response = conversor.obterDados(json, GutendexResponseDto.class);
            this.lastSearchResults = response.getResults();

            if(this.lastSearchResults.isEmpty()) {
                System.out.println("Nenhum livro encontrado com esse título: " + title);
                return;
            }

            this.lastSearchResults.forEach(book -> {
                if(!allFoundBooks.stream().anyMatch(b -> b.equals(book))) {
                    allFoundBooks.add(book);
                }
            });

            saveToDatabase(this.lastSearchResults);
            displayBooks(this.lastSearchResults);
        } catch (Exception e) {
            System.out.println("Erro de pesquisa: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveToDatabase(List<BookDTO> books) {
        try {
            books.forEach(bookDTO -> {
                if (bookDTO.getAuthors() != null && !bookDTO.getAuthors().isEmpty()) {
                    AuthorDTO authorDTO = bookDTO.getAuthors().get(0);

                    Author author = authorRepository.findByName(authorDTO.getName())
                            .orElseGet(() -> {
                                Author newAuthor = new Author();
                                newAuthor.setName(authorDTO.getName());
                                newAuthor.setBirthYear(authorDTO.getBirthYear());
                                newAuthor.setDeathYear(authorDTO.getDeathYear());
                                return authorRepository.save(newAuthor);
                            });

                    if (!bookRepository.existsByTitleAndAuthor(bookDTO.getTitle(), author)) {
                        Book book = new Book();
                        book.setTitle(bookDTO.getTitle());
                        book.setLanguage(bookDTO.getLanguages() != null && !bookDTO.getLanguages().isEmpty() ?
                                bookDTO.getLanguages().get(0) : "Desconhecido..");
                        book.setDownloadCount(bookDTO.getDownloadCount());
                        book.setAuthor(author);
                        bookRepository.save(book);
                    }
                }
            });
        } catch (Exception e) {
            System.out.println("Erro ao salvar banco de dados: " + e.getMessage());
        }
    }

    private void listRegisteredBooks() {
        if(this.allFoundBooks.isEmpty()) {
            System.out.println("Nenhum livro registrado ainda. Por favor, use a pesquisa (opcao 1).");
            return;
        }
        System.out.println("\n=== TODOS OS LIVROS REGISTRADOS (" + this.allFoundBooks.size() + ") ===");
        displayBooks(this.allFoundBooks);
    }

    private void listRegisteredAuthors() {
        if (allFoundBooks.isEmpty()) {
            System.out.println("Nenhum livro registrado ainda. Por favor, use a pesquisa (opcao 1).");
            return;
        }

        System.out.println("\n=== AUTORES REGISTRADOS ===");
        allFoundBooks.stream()
                .flatMap(book -> book.getAuthors().stream())
                .collect(Collectors.groupingBy(AuthorDTO::getName,
                        Collectors.reducing((a1, a2) -> a1)))
                .values()
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted(Comparator.comparing(AuthorDTO::getName))
                .forEach(author -> {
                    System.out.println("---- AUTOR ----");
                    System.out.println("Nome: " + author.getName());
                    if (author.getBirthYear() != null && author.getDeathYear() != null) {
                        System.out.println("Nascimento: " + author.getBirthYear() +
                                ", Morte: " + author.getDeathYear());
                    }
                    System.out.println("_______________");
                });
    }

    private void listAuthorsAliveInYear() {
        if (allFoundBooks.isEmpty()) {
            System.out.println("Nenhum livro registrado ainda. Por favor, use a pesquisa (opcao 1).");
            return;
        }

        System.out.println("Digite um ano para ver os autores vivos:");
        try {
            int year = scanner.nextInt();
            scanner.nextLine();

            System.out.println("\n=== AUTORES VIVOS EM " + year + " ===");
            allFoundBooks.stream()
                    .flatMap(book -> book.getAuthors().stream())
                    .collect(Collectors.groupingBy(AuthorDTO::getName,
                            Collectors.reducing((a1, a2) -> a1)))
                    .values()
                    .stream()
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .filter(author -> author.getBirthYear() != null &&
                            author.getDeathYear() != null &&
                            author.getBirthYear() <= year &&
                            author.getDeathYear() >= year)
                    .sorted(Comparator.comparing(AuthorDTO::getName))
                    .forEach(author -> {
                        System.out.println("---- AUTOR ----");
                        System.out.println("Nome: " + author.getName());
                        System.out.println("Nascimento: " + author.getBirthYear() +
                                ", Morte: " + author.getDeathYear());
                        System.out.println("Nasceu em " + year + ": " +
                                (year - author.getBirthYear()) + " Anos");
                        System.out.println("_______________");
                    });
        } catch (Exception e) {
            System.out.println("Ano invalido. digite um número (ex: 1800).");
            scanner.nextLine();
        }
    }

    private void listBooksByLanguage() {
        if (allFoundBooks.isEmpty()) {
            System.out.println("Nenhum livro registrado ainda. Por favor, usa pesquisa (opcao 1).");
            return;
        }

        System.out.println("Digite o idioma para usar o filtro (ex: pt, en, fr):");
        var language = scanner.nextLine().toLowerCase();

        System.out.println("\n=== LIVROS EM " + language.toUpperCase() + " ===");
        List<BookDTO> filteredBooks = allFoundBooks.stream()
                .filter(book -> book.getLanguages() != null &&
                        book.getLanguages().stream()
                                .anyMatch(lang -> lang.equalsIgnoreCase(language)))
                .toList();

        if (filteredBooks.isEmpty()) {
            System.out.println("Nenhum livro encontrado em " + language + " idioma");
        } else {
            displayBooks(filteredBooks);
        }
    }

    private void displayBooks(List<BookDTO> books) {
        if(books == null || books.isEmpty()) {
            System.out.println("Nenhum livro para exibir.");
            return;
        }

        System.out.println("\n=== LIVROS ENCONTRADOS " + books.size() +" ===");
        books.forEach(book -> {
            System.out.println("\n---- LIVRO ----");
            System.out.println("Título: " + book.getTitle());

            String language = (book.getLanguages() != null && !book.getLanguages().isEmpty())
                    ? book.getLanguages().get(0) : "Idioma desconhecido";
            System.out.println("Idioma: " + language);

            System.out.println("Downloads: " + book.getDownloadCount());

            if(book.getAuthors() != null && !book.getAuthors().isEmpty()) {
                var author = book.getAuthors().get(0);
                System.out.println("Autor: " + author.getName());
                if(author.getBirthYear() != null && author.getDeathYear() != null) {
                    System.out.printf("Período de vida: %d - %d (%d anos)\n",
                            author.getBirthYear(),
                            author.getDeathYear(),
                            author.getDeathYear() - author.getBirthYear());
                }
            }
            System.out.println("_______________");
        });
        System.out.println("=== FIM DOS RESULTADOS ===");
    }
}
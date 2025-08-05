package br.com.gutendex.api.repository;

import br.com.gutendex.api.model.Author;
import br.com.gutendex.api.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByTitleAndAuthor(String title, Author author);
}


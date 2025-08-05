package br.com.gutendex.api;

import br.com.gutendex.api.dto.BookDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GutendexResultsDto {

    private int count;

    private String next;

    private String previous;

    @JsonProperty("results")
    private List<BookDTO> livros;

    public GutendexResultsDto() {}

    public int getCount() {
        return count;
    }

    public String getNext() {
        return next;
    }

    public String getPrevious() {
        return previous;
    }

    public List<BookDTO> getLivros() {
        return livros;
    }
}

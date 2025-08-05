// BookDTO.java
package br.com.gutendex.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BookDTO {
    @JsonProperty("title")
    private String title;

    @JsonProperty("languages")
    private List<String> languages;

    @JsonProperty("download_count")
    private int downloadCount;

    @JsonProperty("authors")
    private List<AuthorDTO> authors;

    public String getTitle() {
        return title;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public List<AuthorDTO> getAuthors() {
        return authors;
    }

    public BookDTO() {}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookDTO bookDTO = (BookDTO) o;
        return downloadCount == bookDTO.downloadCount &&
                Objects.equals(title, bookDTO.title) &&
                Objects.equals(languages, bookDTO.languages) &&
                Objects.equals(authors, bookDTO.authors);
    }

}
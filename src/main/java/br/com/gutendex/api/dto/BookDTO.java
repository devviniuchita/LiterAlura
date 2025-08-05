package br.com.gutendex.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
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
}